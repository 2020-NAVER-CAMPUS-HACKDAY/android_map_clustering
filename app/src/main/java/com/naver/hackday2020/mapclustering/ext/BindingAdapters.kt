package com.naver.hackday2020.mapclustering.ext

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.naver.hackday2020.mapclustering.R

@BindingAdapter("bind_image")
fun ImageView.bindImage(url: String?) {
    Glide.with(context)
        .load(url)
        .error(R.drawable.no_image)
        .into(this)
}

@BindingAdapter("category")
fun TextView.category(text: String?) {
    text?.let {
        val category = "# $it"
        this.text = category
    }
}