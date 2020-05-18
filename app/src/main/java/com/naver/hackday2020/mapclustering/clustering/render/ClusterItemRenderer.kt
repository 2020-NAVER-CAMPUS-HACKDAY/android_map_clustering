package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.maps.map.NaverMap

class ClusterItemRenderer<ITEM : ClusterItem>(
    private val naverMap: NaverMap
) : Renderer<ITEM> {
    private val items = arrayListOf<ITEM>()
    private var onClickEvent: (clusterItem: ClusterItem) -> Unit = { }

    override fun update(newClusters: List<Cluster<ITEM>>) {
        for (newCluster in newClusters) {
            items.addAll(newCluster.items)
        }
    }

    override fun clear() {
        for (item in items) {
            item.hide()
        }
        items.clear()
    }

    override fun setUpMarkers() {
        for (item in items) {
            item.show(naverMap)
        }
        setOnClusterItemClickListener(onClickEvent)
    }

    override fun setOnClusterItemClickListener(onClick: (clusterItem: ClusterItem) -> Unit) {
        onClickEvent = onClick
        for (item in items) {
            item.setOnClickListener(onClick)
        }
    }

    override fun setOnClusterClickListener(onClick: (cluster: Cluster<ITEM>) -> Unit) {
        // do nothing
    }
}