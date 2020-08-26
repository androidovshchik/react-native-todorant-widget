package com.todorant.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.todorant.widget.extensions.getColorRes
import org.jetbrains.anko.dip

private val paint = Paint().apply {
    style = Paint.Style.STROKE
}

fun Context.drawLines(widgetWidth: Int, allCount: Int, doneCount: Int): Bitmap {
    val red = getColorRes(R.color.widgetLineRed)
    val gray = getColorRes(R.color.widgetLineGray)
    val maxCount = widgetWidth / dip(8) / 2
    Log.v(TAG, "drawLines $widgetWidth $maxCount")
    return Bitmap.createBitmap(widgetWidth, dip(2), Bitmap.Config.ARGB_8888).apply {
        val w = width.toFloat()
        val h = height.toFloat()
        paint.strokeWidth = h
        with(Canvas(this)) {
            if (allCount > maxCount) {
                if (doneCount < allCount) {
                    val doneWidth = w * doneCount / allCount
                    paint.color = red
                    drawLine(0f, h / 2, doneWidth, h / 2, paint)
                    paint.color = gray
                    drawLine(doneWidth, h / 2, w, h / 2, paint)
                } else {
                    drawColor(red)
                }
            } else {
                val steps = allCount * 10 - 1
                val spaceWidth = widgetWidth / steps.toFloat()
                val stepWidth = spaceWidth * 10
                for (i in 1..allCount) {
                    paint.color = if (i <= doneCount) red else gray
                    drawLine((i - 1) * stepWidth, h / 2, i * stepWidth - spaceWidth, h / 2, paint)
                }
            }
        }
    }
}