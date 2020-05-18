package com.naver.hackday2020.mapclustering.model

import com.naver.hackday2020.OnDataLoadedCallback
import com.naver.hackday2020.mapclustering.MapApp

object PlaceDataProvider {
    private const val placeAssetName = "restaurant-list-for-test.json"
    private val assetDataProvider = MapApp.assetDataProvider
    private val categoryList = hashMapOf<String, Int>()
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
                    placeList = data
                    isDataReady = true
                }

                override fun onDataLoadFailed() {
                    failed()
                    isDataReady = false
                }
            }
        )
    }

    fun getCategoryData(
        success: (categoryList: HashMap<String, Int>) -> Unit,
        failed: () -> Unit
    ) {
        if (isDataReady) {
            success(categoryList)
            return
        }

        getAllData({
            initCategory()
            success(categoryList)
        }, failed)
    }

    private fun initCategory() {
        for (place in placeList.places) {
            categoryList[place.category] = categoryList[place.category]?.plus(1) ?: 1
        }
    }
}