package com.naver.hackday2020

import androidx.test.core.app.ApplicationProvider
import com.naver.hackday2020.data.Place
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class AssetDataProviderTest {
    private val assetDataProvider = AssetDataProvider(ApplicationProvider.getApplicationContext())
    private val sampleFileName = "json-sample.txt"
    private val samplePlace = Place(
        0f,
        "경기도 성남시 분당구 정자동 178-1",
        "그린팩토리",
        "",
        "경기도 성남시 분당구 불정로 6 그린팩토리",
        "HGL0_mYBGUvljfTILX-R",
        "127.1054328",
        "37.3595963"
    )

    @Test
    fun receiveData() {
        val data = assetDataProvider.receiveData(sampleFileName, Place.serializer())
        assertEquals(samplePlace, data)
    }

    @Test
    fun receiveDataAsync() {
        assetDataProvider.receiveDataAsync(
            sampleFileName,
            Place.serializer(),
            object : OnDataLoadedCallback<Place> {
                override fun onDataReady(data: Place) {
                    assertEquals(data, samplePlace)
                }

                override fun onDataLoadFailed() {
                    fail()
                }
            })
    }
}