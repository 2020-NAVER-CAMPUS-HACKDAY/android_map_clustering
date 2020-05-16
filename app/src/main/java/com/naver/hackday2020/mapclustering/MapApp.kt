package com.naver.hackday2020.mapclustering

import android.app.Application
import android.content.Context
import com.naver.hackday2020.AssetDataProvider

class MapApp : Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var assetDataProvider: AssetDataProvider
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        assetDataProvider = AssetDataProvider(applicationContext)
    }
}