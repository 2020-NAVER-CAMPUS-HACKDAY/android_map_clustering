package com.naver.hackday2020

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.serialization.DeserializationStrategy
import java.nio.charset.StandardCharsets

class AssetDataProvider(private val context: Context) {
    var charset: String = StandardCharsets.UTF_8.name()

    @WorkerThread
    fun <T> receiveData(
        assetFileName: String,
        resultTypeSerializer: DeserializationStrategy<T>
    ): T? {
        val content = AssetFileReader(context).extractContent(assetFileName, charset)
        return JsonConverter.jsonToObject(resultTypeSerializer, content)
    }

    fun <T> receiveDataAsync(
        assetFileName: String,
        resultTypeSerializer: DeserializationStrategy<T>,
        onDataLoadedCallback: OnDataLoadedCallback<T>
    ) {
        TaskExecutors.runOnWorkerThread {
            val data = receiveData(assetFileName, resultTypeSerializer)

            TaskExecutors.runOnMainThread {
                if (data != null) {
                    onDataLoadedCallback.onDataReady(data)
                } else {
                    onDataLoadedCallback.onDataLoadFailed()
                }
            }
        }
    }
}