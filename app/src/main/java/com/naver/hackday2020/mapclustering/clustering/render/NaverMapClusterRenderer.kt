package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.clustering.algo.StaticCluster
import com.naver.maps.map.NaverMap

class NaverMapClusterRenderer<T : ClusterItem>(
    naverMap: NaverMap
) {
    private val clusterItemRenderer = PlaceItemRenderer<T>(naverMap)
    private val clusterRenderer = ClusterRenderer<T>(naverMap)

    fun changeClusters(newClusters: Set<Cluster<T>>) {
        clear()
        clusterItemRenderer.update(getPlaceItems(newClusters))
        clusterRenderer.update(getStaticClusters(newClusters))
        setUpMarkers()
    }

    fun clear() {
        clusterItemRenderer.clear()
        clusterRenderer.clear()
    }

    fun setUpMarkers() {
        clusterItemRenderer.setUpMarkers()
        clusterRenderer.setUpMarkers()
    }

    fun setOnClusterItemClickListener(onClick: (clusterItem: T) -> Unit) {
        clusterItemRenderer.setOnPlaceItemClickListener {
            (it as? T)?.run { onClick(this) }
        }
    }

    fun setOnClusterClickListener(onClick: (cluster: Cluster<T>) -> Unit) {
        clusterRenderer.setOnClusterClickListener(onClick)
    }

    private fun getPlaceItems(clusters: Set<Cluster<T>>) = clusters.filter {
        (it as? StaticCluster<*>) != null && it.isPlaceItem()
    }

    private fun getStaticClusters(clusters: Set<Cluster<T>>) = clusters.filter {
        (it as? StaticCluster<*>) != null && it.isCluster()
    }
}