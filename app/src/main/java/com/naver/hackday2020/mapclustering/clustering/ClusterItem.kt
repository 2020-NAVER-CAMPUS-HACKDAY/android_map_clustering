package com.naver.hackday2020.mapclustering.clustering

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap

interface ClusterItem {

    fun getPosition(): LatLng

    fun show(naverMap: NaverMap)

    fun hide()

    fun setOnClickListener(onClick: (item: ClusterItem) -> Unit)
}