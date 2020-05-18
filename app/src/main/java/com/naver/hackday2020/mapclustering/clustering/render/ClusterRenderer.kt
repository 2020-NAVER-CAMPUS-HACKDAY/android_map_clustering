package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.clustering.ui.ClusterOverlayView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class ClusterRenderer<ITEM : ClusterItem>(
    private val naverMap: NaverMap
) : Renderer<ITEM> {
    private val clusterMarkers = arrayListOf<Pair<Cluster<ITEM>, Marker>>()
    private var onClickEvent: (cluster: Cluster<ITEM>) -> Unit = { }

    override fun update(newClusters: List<Cluster<ITEM>>) {
        val newClusterMarkers = newClusters.map { Pair(it, createCluster(it)) }
        clusterMarkers.addAll(newClusterMarkers)
    }

    override fun clear() {
        for (marker in clusterMarkers) {
            marker.second.map = null
        }
        clusterMarkers.clear()
    }

    override fun setUpMarkers() {
        for (marker in clusterMarkers) {
            marker.second.map = naverMap
        }
        setOnClusterClickListener(onClickEvent)
    }

    override fun setOnClusterItemClickListener(onClick: (clusterItem: ClusterItem) -> Unit) {
        // do nothing
    }

    override fun setOnClusterClickListener(onClick: (cluster: Cluster<ITEM>) -> Unit) {
        onClickEvent = onClick
        for (clusterMarker in clusterMarkers) {
            clusterMarker.second.setOnClickListener {
                onClick(clusterMarker.first)
                true
            }
        }
    }

    private fun createCluster(item: Cluster<ITEM>): Marker {
        val markerView = ClusterOverlayView.create(item.size)
        return Marker(item.position, OverlayImage.fromView(markerView))
    }
}