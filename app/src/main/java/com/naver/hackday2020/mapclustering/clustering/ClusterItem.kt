package com.naver.hackday2020.mapclustering.clustering

import com.naver.maps.geometry.LatLng

interface ClusterItem {

    fun getPosition(): LatLng
}