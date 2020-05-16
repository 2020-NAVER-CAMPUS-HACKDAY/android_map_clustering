package com.naver.hackday2020.mapclustering.clustering.algo

import android.util.Log
import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.geometry.Bounds
import com.naver.hackday2020.mapclustering.geometry.Point
import com.naver.hackday2020.mapclustering.projection.SphericalMercatorProjection
import com.naver.hackday2020.mapclustering.quadtree.PointQuadTree
import com.naver.maps.geometry.LatLng
import kotlin.math.pow

/**
 * A simple clustering algorithm with O(nlog n) performance. Resulting clusters are not
 * hierarchical.
 *
 * High level algorithm:
 * 1. Iterate over items in the order they were added (candidate clusters).
 * 2. Create a cluster with the center of the item.
 * 3. Add all items that are within a certain distance to the cluster.
 * 4. Move any items out of an existing cluster if they are closer to another cluster.
 * 5. Remove those items from the list of candidate clusters.
 *
 * Clusters have the center of the first element (not the centroid of the items within it).
 */
class NonHierarchicalDistanceBasedAlgorithm<T : ClusterItem> : BaseAlgorithm<T>() {

    /**
     * Any modifications should be synchronized on quadTree.
     */
    private val quadItems = HashSet<QuadItem<T>>()

    /**
     * Any modifications should be synchronized on quadTree.
     */
    private val quadTree = PointQuadTree<QuadItem<T>>(0.0, 1.0, 0.0, 1.0)

    override var maxDistanceBetweenClusteredItems = DEFAULT_MAX_DISTANCE_AT_ZOOM

    override val items: Collection<T>
        get() {
            val items = ArrayList<T>()
            synchronized(quadTree) {
                for (quadItem in quadItems) {
                    items.add(quadItem.clusterItem)
                }
            }
            return items
        }

    override fun addItem(item: T): Boolean {
        var result: Boolean
        val quadItem = QuadItem(item)
        synchronized(quadTree) {
            result = quadItems.add(quadItem)
            if (result) {
                quadTree.add(quadItem)
            }
        }
        return result
    }

    override fun addItems(items: Collection<T>): Boolean {
        var result = false
        for (item in items) {
            val individualResult = addItem(item)
            if (individualResult) {
                result = true
            }
        }
        return result
    }

    override fun clearItems() {
        synchronized(quadTree) {
            quadItems.clear()
            quadTree.clear()
        }
    }

    override fun removeItem(item: T): Boolean {
        var result: Boolean
        // QuadItem delegates hashcode() and equals() to its item so,
        //   removing any QuadItem to that item will remove the item
        val quadItem = QuadItem(item)
        synchronized(quadTree) {
            result = quadItems.remove(quadItem)
            if (result) {
                quadTree.remove(quadItem)
            }
        }
        return result
    }

    override fun removeItems(items: Collection<T>): Boolean {
        var result = false
        synchronized(quadTree) {
            for (item in items) {
                // QuadItem delegates hashcode() and equals() to its item so,
                //   removing any QuadItem to that item will remove the item
                val quadItem = QuadItem(item)
                val individualResult = quadItems.remove(quadItem)
                if (individualResult) {
                    quadTree.remove(quadItem)
                    result = true
                }
            }
        }
        return result
    }

    override fun updateItem(item: T): Boolean {
        var result: Boolean
        synchronized(quadTree) {
            result = removeItem(item)
            if (result) {
                // Only add the item if it was removed (to help prevent accidental duplicates on map)
                result = addItem(item)
            }
        }
        return result
    }

    override fun getClusters(zoom: Double): Set<Cluster<T>> {
        val discreteZoom = zoom.toInt()

        val zoomSpecificSpan = maxDistanceBetweenClusteredItems.toDouble() / 2.0.pow(discreteZoom.toDouble()) / 256.0

        val visitedCandidates = HashSet<QuadItem<T>>()
        val results = HashSet<Cluster<T>>()
        val distanceToCluster = HashMap<QuadItem<T>, Double>()
        val itemToCluster = HashMap<QuadItem<T>, StaticCluster<T>>()

        synchronized(quadTree) {
            for (candidate in quadItems) {
                if (visitedCandidates.contains(candidate)) {
                    // Candidate is already part of another cluster.
                    continue
                }

                val searchBounds = createBoundsFromSpan(candidate.point, zoomSpecificSpan)
                val clusterItems: Collection<QuadItem<T>>
                clusterItems = quadTree.search(searchBounds)
                if (clusterItems.size == 1) {
                    // Only the current marker is in range. Just add the single item to the results.
                    results.add(candidate)
                    visitedCandidates.add(candidate)
                    distanceToCluster[candidate] = 0.0
                    continue
                }
                val cluster = StaticCluster<T>(candidate.clusterItem.getPosition())
                results.add(cluster)

                for (clusterItem in clusterItems) {
                    val existingDistance = distanceToCluster[clusterItem]
                    val distance = distanceSquared(clusterItem.point, candidate.point)
                    if (existingDistance != null) {
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingDistance < distance) {
                            continue
                        }
                        // Move item to the closer cluster.
                        itemToCluster[clusterItem]!!.remove(clusterItem.clusterItem)
                    }
                    distanceToCluster[clusterItem] = distance
                    cluster.add(clusterItem.clusterItem)
                    itemToCluster[clusterItem] = cluster
                }
                visitedCandidates.addAll(clusterItems)
            }
        }
        // 결과 확인용 테스트 로그
        Log.d(tag, "results = $results")
        return results
    }

    private fun distanceSquared(a: Point, b: Point): Double {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)
    }

    private fun createBoundsFromSpan(p: Point, span: Double): Bounds {
        val halfSpan = span / 2
        return Bounds(
            p.x - halfSpan, p.x + halfSpan,
            p.y - halfSpan, p.y + halfSpan
        )
    }

    data class QuadItem<T : ClusterItem>(val clusterItem: T) : PointQuadTree.Item, Cluster<T> {
        override val point: Point
        override val position: LatLng = clusterItem.getPosition()
        override val items: Set<T>

        override val size
            get() = 1

        init {
            point = PROJECTION.toPoint(position)
            items = setOf(clusterItem)
        }
    }

    companion object {
        private const val DEFAULT_MAX_DISTANCE_AT_ZOOM = 100 // essentially 100 dp.
        private val PROJECTION = SphericalMercatorProjection(1.0)
        private const val tag = "Algorithm" // 테스트 로그용 tag
    }
}