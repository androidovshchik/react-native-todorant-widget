package com.todorant.widget

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class WidgetModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "TodorantWidget"

    @ReactMethod
    fun toggle(enable: Boolean) {
        TodorantWidget.toggle(reactApplicationContext, enable)
    }

    @ReactMethod
    fun forceUpdateAll() {
        TodorantWidget.forceUpdateAll(reactApplicationContext)
    }
}