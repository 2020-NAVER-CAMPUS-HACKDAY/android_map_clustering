package com.naver.hackday2020.mapclustering.model


import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceInfo(
    @SerialName("places")
    val places: List<Place> = listOf()
) {

    fun getMarkers(): List<Marker> = places.map { item ->
        Marker(LatLng(item.y.toDouble(), item.x.toDouble()))
    }
}