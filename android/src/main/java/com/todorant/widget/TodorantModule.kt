package com.todorant.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.react.bridge.*

class TodorantModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext),
    ActivityEventListener {

    private var args: Bundle? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName() = "TodorantWidget"

    override fun onNewIntent(intent: Intent?) {
        args = intent?.extras
    }

    @ReactMethod
    fun toggle(enable: Boolean) {
        TodorantProvider.toggle(reactApplicationContext, enable)
    }

    @ReactMethod
    fun forceUpdateAll() {
        TodorantProvider.updateAll(reactApplicationContext, true)
    }

    @ReactMethod
    fun getNewArgs(callback: Callback) {
        val args = args ?: currentActivity?.intent?.extras
        try {
         callback.invoke(if (args != null) Arguments.fromBundle(args) else null)
        } catch (e: Throwable) {
            // Do nothing
        }
    }

    override fun onActivityResult(
        activity: Activity?,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
    }
}