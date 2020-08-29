package com.todorant.widget

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class TodorantModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "TodorantWidget"

    @ReactMethod
    fun toggle(enable: Boolean) {
        TodorantProvider.toggle(reactApplicationContext, enable)
    }

    @ReactMethod
    fun forceUpdateAll() {
        TodorantProvider.updateAll(reactApplicationContext, true)
    }
}