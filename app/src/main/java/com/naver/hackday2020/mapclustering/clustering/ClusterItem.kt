package com.naver.hackday2020.mapclustering.clustering

import com.naver.maps.geometry.LatLng

/**
 * ClusterItem represents a marker on the map.
 */
interface ClusterItem {
    /**
     * The position of this marker. This must always return the same value.
     */
    fun getPosition(): LatLng
}