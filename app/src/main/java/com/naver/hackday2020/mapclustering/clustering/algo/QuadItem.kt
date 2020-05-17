package com.naver.hackday2020.mapclustering.clustering.algo

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.geometry.Point
import com.naver.hackday2020.mapclustering.quadtree.PointQuadTree
import com.naver.maps.geometry.LatLng


data class QuadItem<T : ClusterItem>(val clusterItem: T) : PointQuadTree.Item, Cluster<T> {
    override val point: Point
    override val position: LatLng = clusterItem.getPosition()
    override val items: Set<T>

    override val size
        get() = 1

    init {
        point = NonHierarchicalDistanceBasedAlgorithm.projection.toPoint(position)
        items = setOf(clusterItem)
    }
}