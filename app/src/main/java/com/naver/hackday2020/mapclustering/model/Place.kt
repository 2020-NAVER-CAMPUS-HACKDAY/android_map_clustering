package com.naver.hackday2020.mapclustering.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerialName("category")
    val category: String,
    @SerialName("id")
    val id: String,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("name")
    val name: String,
    @SerialName("x")
    val x: Double,
    @SerialName("y")
    val y: Double
)