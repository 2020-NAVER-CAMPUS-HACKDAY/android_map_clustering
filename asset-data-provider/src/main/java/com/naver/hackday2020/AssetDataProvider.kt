package com.naver.hackday2020

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.serialization.DeserializationStrategy
import java.io.IOException
import java.nio.charset.StandardCharsets

class AssetDataProvider(private val context: Context) {

    companion object {
        private val tag = AssetDataProvider::class.java.name
    }

    /**
     * json 파일의 내용을 읽고 [T] 타입으로 변환합니다.
     */
    @WorkerThread
    fun <T> receiveData(
        assetFileName: String,
        resultTypeSerializer: DeserializationStrategy<T>,
        charset: String = StandardCharsets.UTF_8.name()
    ): T? {
        val content = AssetFileReader(context).extractContent(assetFileName, charset)
        return JsonConverter.jsonToObject(resultTypeSerializer, content)
    }

    /**
     * 대용량 파일일 경우 해당 메서드를 사용하는 것을 권장합니다.
     */
    @WorkerThread
    fun <T> receiveData(
        assetFileName: String,
        resultTypeItem: Readable<T>,
        charset: String = StandardCharsets.UTF_8.name()
    ): T? {
        val reader = AssetFileReader(context).getJsonReader(assetFileName, charset) ?: return null

        return try {
            reader.use(resultTypeItem::readItem)
        } catch (e: IOException) {
            Log.e(tag, "failed to readItem. ${e.message}")
            null
        }
    }

    /**
     * json 파일의 내용을 읽고 [T] 타입으로 변환합니다.
     * 백그라운드에서 변환 처리 후, 실행 결과를 [OnDataLoadedCallback]에서 사용할 수 있습니다.
     */
    fun <T> receiveDataAsync(
        assetFileName: String,
        resultTypeSerializer: DeserializationStrategy<T>,
        onDataLoadedCallback: OnDataLoadedCallback<T>,
        charset: String = StandardCharsets.UTF_8.name()
    ) {
        TaskExecutors.runOnWorkerThread {
            val data = receiveData(assetFileName, resultTypeSerializer, charset)
            handleResultData(data, onDataLoadedCallback)
        }
    }

    /**
     * 대용량 파일일 경우 해당 메서드를 사용하는 것을 권장합니다.
     */
    fun <T> receiveDataAsync(
        assetFileName: String,
        resultTypeItem: Readable<T>,
        onDataLoadedCallback: OnDataLoadedCallback<T>,
        charset: String = StandardCharsets.UTF_8.name()
    ) {
        TaskExecutors.runOnWorkerThread {
            val data = receiveData(assetFileName, resultTypeItem, charset)
            handleResultData(data, onDataLoadedCallback)
        }
    }

    private fun <T> handleResultData(data: T?, onDataLoadedCallback: OnDataLoadedCallback<T>) {
        TaskExecutors.runOnMainThread {
            if (data != null) {
                onDataLoadedCallback.onDataReady(data)
            } else {
                onDataLoadedCallback.onDataLoadFailed()
            }
        }
    }
}