package com.naver.hackday2020.mapclustering.util

import android.util.TypedValue
import androidx.annotation.Dimension
import com.naver.hackday2020.mapclustering.MapApp


fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    MapApp.appContext.resources.displayMetrics
).toInt()