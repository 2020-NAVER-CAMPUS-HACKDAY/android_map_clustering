package com.naver.hackday2020.mapclustering.util

import android.widget.Toast
import com.naver.hackday2020.mapclustering.MapApp

object ToastUtil {
    fun showToast(
        message: String,
        length: Int = Toast.LENGTH_LONG
    ) {
        Toast.makeText(MapApp.appContext, message, length).show()
    }
}