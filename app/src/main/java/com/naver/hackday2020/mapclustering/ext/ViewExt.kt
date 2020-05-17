package com.naver.hackday2020.mapclustering.ext

import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
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

fun View.showToast(
    message: String,
    length: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(this.context, message, length).show()
}