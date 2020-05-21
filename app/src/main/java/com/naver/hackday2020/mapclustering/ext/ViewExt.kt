package com.naver.hackday2020.mapclustering.ext

import android.graphics.PointF
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.naver.hackday2020.mapclustering.R

fun View.showSnack(
    @StringRes resId: Int,
    length: Int = Snackbar.LENGTH_LONG,
    @ColorRes textColor: Int? = null
) {
    showSnack(context.getString(resId), length, textColor)
}

fun View.showSnack(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    @ColorRes textColor: Int? = null
) {
    Snackbar.make(this, message, length).run {
        textColor?.let { setActionTextColor(ContextCompat.getColor(context, textColor)) }
        setAction(context.getString(R.string.ok)) { dismiss() }
        show()
    }
}

fun View.expandBottomSheet() {
    try {
        BottomSheetBehavior.from(this).state = BottomSheetBehavior.STATE_EXPANDED
    } catch (e: IllegalArgumentException) {
        Log.e("View.expandBottomSheet", e.message ?: "IllegalArgumentException occurs")
        e.printStackTrace()
    }
}

fun View.hideBottomSheet() {
    try {
        BottomSheetBehavior.from(this).state = BottomSheetBehavior.STATE_HIDDEN
    } catch (e: IllegalArgumentException) {
        Log.e("View.hideBottomSheet", e.message ?: "IllegalArgumentException occurs")
        e.printStackTrace()
    }
}

fun View.isExpanded() = try {
    BottomSheetBehavior.from(this).state == BottomSheetBehavior.STATE_EXPANDED
} catch (e: IllegalArgumentException) {
    Log.e("View.isExpanded", e.message ?: "IllegalArgumentException occurs")
    false
}

fun View.getCenter() = PointF(x + width / 2f, y + height / 2)