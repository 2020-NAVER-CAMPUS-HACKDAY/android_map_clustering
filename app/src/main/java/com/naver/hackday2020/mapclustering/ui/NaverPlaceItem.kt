package com.naver.hackday2020.mapclustering.ui

import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.hackday2020.mapclustering.model.PlaceList
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

class NaverPlaceItem(
    val place: Place
) : ClusterItem {
    val marker: Marker = Marker(getPosition())

    companion object {
        private val tag = NaverPlaceItem::class.java.name

        fun from(place: Place) = NaverPlaceItem(place)

        fun fromList(placeList: PlaceList) = placeList.places.map { from(it) }
    }

    override fun getPosition() = LatLng(place.y, place.x)

    fun setOnClickListener(onClick: (item: NaverPlaceItem) -> Unit) {
        marker.setOnClickListener {
            onClick(this)
            true
        }
    }

    fun setVisibility(isVisible: Boolean) {
        marker.isVisible = isVisible
    }
}