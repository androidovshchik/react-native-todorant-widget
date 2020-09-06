package com.todorant.widget.extensions

import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

fun RemoteViews.setEnabled(viewId: Int, enabled: Boolean) {
    setBoolean(viewId, "setEnabled", enabled)
}

fun RemoteViews.setBackgroundColor(viewId: Int, @ColorInt value: Int) {
    setInt(viewId, "setBackgroundColor", value)
}

fun RemoteViews.setBackgroundRes(viewId: Int, @DrawableRes value: Int) {
    setInt(viewId, "setBackgroundResource", value)
}