package com.naver.hackday2020.mapclustering.ui

import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.hackday2020.mapclustering.model.PlaceList
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class NaverPlaceItem(
    val place: Place
) : ClusterItem {
    val marker = Marker(getPosition(), OverlayImage.fromResource(R.drawable.ic_restaurant))

    companion object {
        private val tag = NaverPlaceItem::class.java.name

        fun from(place: Place) = NaverPlaceItem(place)

        fun fromList(placeList: PlaceList) = placeList.places.map { from(it) }
    }

    override fun getPosition() = LatLng(place.y, place.x)

    override fun show(naverMap: NaverMap) {
        marker.map = naverMap
    }

    override fun hide() {
        marker.map = null
    }

    override fun setOnClickListener(onClick: (item: ClusterItem) -> Unit) {
        marker.setOnClickListener {
            onClick(this)
            true
        }
    }
}