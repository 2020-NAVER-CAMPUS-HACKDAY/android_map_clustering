package com.naver.hackday2020.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerialName("distance")
    val distance: Float = 0f,
    @SerialName("jibun_address")
    val jibunAddress: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("phone_number")
    val phoneNumber: String = "",
    @SerialName("road_address")
    val roadAddress: String = "",
    @SerialName("sessionId")
    val sessionId: String = "",
    @SerialName("x")
    val x: String = "",
    @SerialName("y")
    val y: String = ""
)