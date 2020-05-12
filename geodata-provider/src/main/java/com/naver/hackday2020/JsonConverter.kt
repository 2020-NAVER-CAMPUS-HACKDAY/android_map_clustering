package com.naver.hackday2020

import android.util.Log
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonDecodingException

object JsonConverter {
    private val tag = JsonConverter::class.java.name
    private val json = Json(JsonConfiguration.Stable)

    fun <T> jsonToObject(serializer: DeserializationStrategy<T>, jsonString: String): T? = try {
        json.parse(serializer, jsonString)
    } catch (e: JsonDecodingException) {
        Log.e(tag, "jsonString is not a valid json format")
        null
    }
}