package com.naver.hackday2020.data


import android.util.JsonReader
import com.naver.hackday2020.Readable


data class ReadablePlace(
    val name: String = "",
    val roadAddress: String = "",
    val jibunAddress: String = "",
    val phoneNumber: String = "",
    val x: String = "",
    val y: String = "",
    val distance: Float = 0f,
    val sessionId: String = ""
) : Readable<ReadablePlace> {

    override fun readItem(reader: JsonReader): ReadablePlace {
        var name = ""
        var roadAddress = ""
        var jibunAddress = ""
        var phoneNumber = ""
        var distance = 0f
        var x = ""
        var y = ""
        var sessionId = ""

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "name" -> name = reader.nextString()
                "road_address" -> roadAddress = reader.nextString()
                "jibun_address" -> jibunAddress = reader.nextString()
                "phone_number" -> phoneNumber = reader.nextString()
                "distance" -> distance = reader.nextDouble().toFloat()
                "x" -> x = reader.nextString()
                "y" -> y = reader.nextString()
                "sessionId" -> sessionId = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return ReadablePlace(
            name, roadAddress, jibunAddress, phoneNumber, x, y, distance, sessionId
        )
    }
}
