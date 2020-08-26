package com.todorant.widget.extensions

import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

fun RemoteViews.toggle(viewId: Int, enable: Boolean) {
    setBoolean(viewId, "setEnabled", enable)
}

fun RemoteViews.setBackgroundColor(viewId: Int, @ColorInt value: Int) {
    setInt(viewId, "setBackgroundColor", value)
}

fun RemoteViews.setBackgroundRes(viewId: Int, @DrawableRes value: Int) {
    setInt(viewId, "setBackgroundResource", value)
}