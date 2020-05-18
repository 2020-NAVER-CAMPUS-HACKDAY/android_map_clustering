package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.maps.map.NaverMap

class NaverMapClusterRenderer<ITEM : ClusterItem>(naverMap: NaverMap) : Renderer<ITEM> {
    private val clusterItemRenderer = ClusterItemRenderer<ITEM>(naverMap)
    private val clusterRenderer = ClusterRenderer<ITEM>(naverMap)

    fun changeClusters(newClusters: Set<Cluster<ITEM>>) {
        clear()
        update(newClusters)
        setUpMarkers()
    }

    override fun update(newClusters: Set<Cluster<ITEM>>) {
        clusterItemRenderer.update(getClusterItems(newClusters))
        clusterRenderer.update(getStaticClusters(newClusters))
    }

    override fun clear() {
        clusterItemRenderer.clear()
        clusterRenderer.clear()
    }

    override fun setUpMarkers() {
        clusterItemRenderer.setUpMarkers()
        clusterRenderer.setUpMarkers()
    }

    override fun setOnClusterItemClickListener(onClick: (clusterItem: ClusterItem) -> Unit) {
        clusterItemRenderer.setOnClusterItemClickListener(onClick)
    }

    override fun setOnClusterClickListener(onClick: (cluster: Cluster<ITEM>) -> Unit) {
        clusterRenderer.setOnClusterClickListener(onClick)
    }

    private fun getClusterItems(clusters: Set<Cluster<ITEM>>) =
        clusters.filter { it.isClusterItem() }.toSet()

    private fun getStaticClusters(clusters: Set<Cluster<ITEM>>) =
        clusters.filter { it.isCluster() }.toSet()
}