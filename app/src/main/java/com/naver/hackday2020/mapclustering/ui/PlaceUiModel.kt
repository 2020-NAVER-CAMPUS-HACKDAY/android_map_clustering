package com.naver.hackday2020.mapclustering.ui

import android.util.Log
import com.naver.hackday2020.mapclustering.clustering.ClusterItem
import com.naver.hackday2020.mapclustering.model.Place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

class PlaceUiModel(
    val place: Place
) : ClusterItem {
    val marker: Marker

    companion object {
        private val tag = PlaceUiModel::class.java.name

        fun from(place: Place) = PlaceUiModel(place)
    }

    override fun getPosition() = LatLng(place.y, place.x)

    fun setOnClickListener(onClick: (item: PlaceUiModel) -> Unit) {
        marker.setOnClickListener {
            onClick(this)
            true
        }
    }

    init {
        marker = Marker(getPosition())
        setOnClickListener {
            Log.d(tag, "marker clicked position - x : ${place.x}, y : ${place.y}")
        }
    }
}