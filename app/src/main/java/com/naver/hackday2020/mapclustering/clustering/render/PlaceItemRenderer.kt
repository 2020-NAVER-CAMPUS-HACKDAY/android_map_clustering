package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.maps.map.NaverMap

class PlaceItemRenderer<T : ClusterItem>(
    private val naverMap: NaverMap
) : Renderer<T> {
    private val items = arrayListOf<T>()
    private var onClickEvent: (clusterItem: ClusterItem) -> Unit = { }

    override fun update(newClusters: List<Cluster<T>>) {
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
        setOnPlaceItemClickListener(onClickEvent)
    }

    override fun setOnPlaceItemClickListener(onClick: (clusterItem: ClusterItem) -> Unit) {
        onClickEvent = onClick
        for (item in items) {
            item.setOnClickListener(onClick)
        }
    }

    override fun setOnClusterClickListener(onClick: (cluster: Cluster<T>) -> Unit) {
        // do nothing
    }
}