package com.naver.hackday2020.mapclustering.clustering

import com.naver.maps.geometry.LatLng

/**
 * A collection of ClusterItems that are nearby each other.
 */
interface Cluster<T : ClusterItem> {
    val position: LatLng
    val items: Collection<T>
    val size: Int

    fun isCluster(minClusterSize: Int): Boolean

    fun isClusterItem(minClusterSize: Int): Boolean
}