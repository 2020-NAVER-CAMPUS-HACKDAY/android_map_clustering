package com.naver.hackday2020.mapclustering.clustering.render

import com.naver.hackday2020.mapclustering.clustering.Cluster
import com.naver.hackday2020.mapclustering.clustering.ClusterItem

interface Renderer<ITEM : ClusterItem> {

    fun update(newClusters: List<Cluster<ITEM>>)

    fun clear()

    fun setUpMarkers()

    fun setOnClusterItemClickListener(onClick: (clusterItem: ClusterItem) -> Unit)

    fun setOnClusterClickListener(onClick: (cluster: Cluster<ITEM>) -> Unit)
}
