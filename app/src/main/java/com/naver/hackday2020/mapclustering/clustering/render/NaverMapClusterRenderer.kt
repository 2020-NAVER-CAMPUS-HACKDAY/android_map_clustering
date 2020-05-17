package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.clustering.algo.StaticCluster
import com.naver.maps.map.NaverMap

class NaverMapClusterRenderer<ITEM : ClusterItem>(naverMap: NaverMap) {
    private val clusterItemRenderer = PlaceItemRenderer<ITEM>(naverMap)
    private val clusterRenderer = ClusterRenderer<ITEM>(naverMap)

    fun changeClusters(newClusters: Set<Cluster<ITEM>>) {
        clear()
        clusterItemRenderer.update(getPlaceItems(newClusters))
        clusterRenderer.update(getStaticClusters(newClusters))
        setUpMarkers()
    }

    fun clear() {
        clusterItemRenderer.clear()
        clusterRenderer.clear()
    }

    fun setOnClusterItemClickListener(onClick: (clusterItem: ITEM) -> Unit) {
        clusterItemRenderer.setOnPlaceItemClickListener {
            (it as? ITEM)?.run { onClick(this) }
        }
    }

    fun setOnClusterClickListener(onClick: (cluster: Cluster<ITEM>) -> Unit) {
        clusterRenderer.setOnClusterClickListener(onClick)
    }

    private fun setUpMarkers() {
        clusterItemRenderer.setUpMarkers()
        clusterRenderer.setUpMarkers()
    }

    private fun getPlaceItems(clusters: Set<Cluster<ITEM>>) = clusters.filter {
        (it as? StaticCluster<*>) != null && it.isPlaceItem()
    }

    private fun getStaticClusters(clusters: Set<Cluster<ITEM>>) = clusters.filter {
        (it as? StaticCluster<*>) != null && it.isCluster()
    }
}