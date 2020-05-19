package com.naver.hackday2020.mapclustering.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.naver.hackday2020.mapclustering.MapApp
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.util.StringUtil

enum class MarkerType(@DrawableRes val iconResId: Int, @StringRes val nameResId: Int) {
    RESTAURANT(R.drawable.ic_restaurant, R.string.place_type_restaurant),
    CAFE(R.drawable.ic_cafe, R.string.place_type_cafe),
    BAR(R.drawable.ic_bar, R.string.place_type_bar);

    fun getIconDrawable() = MapApp.appContext.getDrawable(iconResId)

    fun getName() = StringUtil.getString(nameResId)

    companion object {

        fun of(category: String): MarkerType = when {
            StringUtil.getStringArray(R.array.place_bar).contains(category) -> BAR
            StringUtil.getStringArray(R.array.place_cafe).contains(category) -> CAFE
            else -> RESTAURANT
        }
    }
}