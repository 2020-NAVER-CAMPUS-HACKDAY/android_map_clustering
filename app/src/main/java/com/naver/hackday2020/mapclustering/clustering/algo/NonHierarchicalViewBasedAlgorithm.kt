package com.naver.hackday2020.mapclustering.clustering.algo

import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.clustering.algo.geometry.Bounds
import com.naver.hackday2020.mapclustering.clustering.algo.projection.SphericalMercatorProjection
import com.naver.hackday2020.mapclustering.clustering.algo.quadtree.PointQuadTree
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import kotlin.math.pow

class NonHierarchicalViewBasedAlgorithm<T : ClusterItem>(
    private var viewWidth: Int,
    private var viewHeight: Int
) : NonHierarchicalDistanceBasedAlgorithm<T>(), ScreenBasedAlgorithm<T> {

    private var mapCenter: LatLng? = null

    override fun onCameraChange(cameraPosition: CameraPosition) {
        mapCenter = cameraPosition.target
    }

    override fun getClusteringItems(
            quadTree: PointQuadTree<QuadItem<T>>,
            discreteZoom: Int
    ): Collection<QuadItem<T>> {
        return quadTree.search(getVisibleBounds(discreteZoom))
    }

    override fun shouldReClusterOnMapMovement(): Boolean {
        return true
    }

    /**
     * Update view width and height in case map size was changed.
     * You need to recluster all the clusters, to update view state after view size changes.\
     */
    fun updateViewSize(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
    }

    private fun getVisibleBounds(zoom: Int): Bounds {
        if (mapCenter == null) {
            return Bounds(0.0, 0.0, 0.0, 0.0)
        }

        val p = PROJECTION.toPoint(mapCenter!!)

        val halfWidthSpan = viewWidth.toDouble() / 2.0.pow(zoom.toDouble()) / 256.0 / 2.0
        val halfHeightSpan = viewHeight.toDouble() / 2.0.pow(zoom.toDouble()) / 256.0 / 2.0

        return Bounds(
            p.x - halfWidthSpan, p.x + halfWidthSpan,
            p.y - halfHeightSpan, p.y + halfHeightSpan
        )
    }

    companion object {
        private val PROJECTION = SphericalMercatorProjection(1.0)
    }
}
