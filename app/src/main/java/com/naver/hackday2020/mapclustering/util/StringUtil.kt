package com.naver.hackday2020.mapclustering.util

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.naver.hackday2020.mapclustering.MapApp

object StringUtil {
    fun getString(@StringRes resId: Int) = MapApp.appContext.getString(resId)

    fun getStringArray(@ArrayRes resId: Int) = MapApp.appContext.resources.getStringArray(resId)
}