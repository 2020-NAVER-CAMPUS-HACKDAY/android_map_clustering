package com.naver.hackday2020.mapclustering.model

import com.naver.hackday2020.OnDataLoadedCallback
import com.naver.hackday2020.mapclustering.MapApp

object PlaceDataProvider {
    private const val placeAssetName = "restaurant-list-for-test.json"
    private val assetDataProvider = MapApp.assetDataProvider
    private var placeList = PlaceList()
    private var isDataReady = false

    fun getAllData(
        success: (placeList: PlaceList) -> Unit,
        failed: () -> Unit
    ) {
        if (isDataReady) {
            success(placeList)
            return
        }

        assetDataProvider.receiveDataAsync(
            placeAssetName,
            PlaceList.serializer(),
            object : OnDataLoadedCallback<PlaceList> {
                override fun onDataReady(data: PlaceList) {
                    success(data)
                    isDataReady = true
                }

                override fun onDataLoadFailed() {
                    failed()
                    isDataReady = false
                }
            }
        )
    }
}