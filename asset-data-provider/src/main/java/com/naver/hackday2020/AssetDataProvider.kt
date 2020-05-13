package com.naver.hackday2020

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.serialization.DeserializationStrategy
import java.nio.charset.StandardCharsets

class AssetDataProvider(private val context: Context) {

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