package com.todorant.example

import android.content.Intent
import android.os.Bundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate

/**
 * The way of putting the launch args into component properties
 */
class MainDelegate(activity: ReactActivity, mainComponentName: String?) :
    ReactActivityDelegate(activity, mainComponentName) {

    private var args: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        args = plainActivity.intent.extras
        super.onCreate(savedInstanceState)
    }

    /**
     * Afaik not working
     */
    override fun onNewIntent(intent: Intent?): Boolean {
        args = intent?.extras
        return super.onNewIntent(intent)
    }

    override fun getLaunchOptions() = args
}

class MainActivity : ReactActivity() {

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    override fun getMainComponentName() = "TodorantWidget"

    override fun createReactActivityDelegate() = MainDelegate(this, mainComponentName)
}