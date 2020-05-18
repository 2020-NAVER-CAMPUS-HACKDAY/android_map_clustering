package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.maps.map.NaverMap

class NaverMapClusterRenderer<ITEM : ClusterItem>(naverMap: NaverMap) {
    private val clusterItemRenderer = ClusterItemRenderer<ITEM>(naverMap)
    private val clusterRenderer = ClusterRenderer<ITEM>(naverMap)

    fun changeClusters(newClusters: Set<Cluster<ITEM>>) {
        clear()
        clusterItemRenderer.update(getClusterItems(newClusters))
        clusterRenderer.update(getStaticClusters(newClusters))
        setUpMarkers()
    }

    fun clear() {
        clusterItemRenderer.clear()
        clusterRenderer.clear()
    }

    fun setOnClusterItemClickListener(onClick: (clusterItem: ITEM) -> Unit) {
        clusterItemRenderer.setOnClusterItemClickListener {
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

    private fun getClusterItems(clusters: Set<Cluster<ITEM>>) = clusters.filter {
        it.isClusterItem()
    }

    private fun getStaticClusters(clusters: Set<Cluster<ITEM>>) = clusters.filter {
        it.isCluster()
    }
}