package com.naver.hackday2020.mapclustering.clustering.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.naver.hackday2020.mapclustering.databinding.LayoutImageMarkerBinding

class PlaceOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = LayoutImageMarkerBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root.apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        })
    }
}