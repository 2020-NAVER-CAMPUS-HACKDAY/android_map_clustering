package com.naver.hackday2020.mapclustering.clustering.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.naver.hackday2020.mapclustering.MapApp
import com.naver.hackday2020.mapclustering.databinding.LayoutImageMarkerBinding
import com.naver.hackday2020.mapclustering.ui.MarkerType
import com.naver.hackday2020.mapclustering.ui.NaverPlaceItem

class PlaceOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = LayoutImageMarkerBinding.inflate(LayoutInflater.from(context))
    private var markerType: MarkerType = MarkerType.RESTAURANT
        set(value) {
            field = value
            binding.ivPlaceType.apply {
                setImageDrawable(markerType.getIconDrawable())
            }
        }

    init {
        addView(binding.root)
    }

    companion object {

        fun create(placeItem: NaverPlaceItem) = PlaceOverlayView(MapApp.appContext).apply {
            markerType = MarkerType.of(placeItem.place.category)
        }
    }
}