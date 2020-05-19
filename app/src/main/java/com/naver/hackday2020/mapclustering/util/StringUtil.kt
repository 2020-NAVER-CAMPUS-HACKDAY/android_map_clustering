package com.naver.hackday2020.mapclustering.util

import androidx.annotation.StringRes
import com.naver.hackday2020.mapclustering.MapApp

object StringUtil {
    fun getString(@StringRes resId: Int) = MapApp.appContext.getString(resId)
}