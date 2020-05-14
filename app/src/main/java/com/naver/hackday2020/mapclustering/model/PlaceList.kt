package com.naver.hackday2020.mapclustering.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceList(
    @SerialName("places")
    val places: List<Place> = listOf()
)