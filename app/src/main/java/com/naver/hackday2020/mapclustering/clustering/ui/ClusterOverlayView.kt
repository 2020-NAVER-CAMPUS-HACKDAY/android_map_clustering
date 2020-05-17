package com.naver.hackday2020.mapclustering.clustering.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.naver.hackday2020.mapclustering.MapApp
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.ext.getCenter
import com.naver.hackday2020.mapclustering.util.dpToPx
import kotlin.math.min

class ClusterOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    var circleColor = context.getColor(R.color.marker_color)
        set(@ColorRes value) {
            field = context.getColor(value)
            invalidate()
        }
    var count = 0
        set(@IntRange(from = 0) value) {
            field = value
            text = count.toString()
        }

    private val defaultTextSize = 16f
    private val defaultPadding = dpToPx(10)
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        textSize = defaultTextSize
        setTextColor(Color.WHITE)
        setPadding(defaultPadding)
    }

    override fun onDraw(canvas: Canvas?) {
        val diameter = min(width, height)
        val radius = diameter / 2f
        val center = getCenter()

        height = diameter
        width = diameter
        paint = paint.apply { color = circleColor }
        canvas?.drawCircle(center.x, center.y, radius, paint)
        super.onDraw(canvas)
    }

    companion object {

        fun create(@IntRange(from = 0) itemCount: Int) =
            ClusterOverlayView(MapApp.appContext).apply { count = itemCount }
    }
}