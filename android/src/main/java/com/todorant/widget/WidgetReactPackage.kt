package com.todorant.widget

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class WidgetReactPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(WidgetModule(reactContext))
    }

    override fun createJSModules() = emptyList<Class<out JavaScriptModule?>>()

    override fun createViewManagers(reactContext: ReactApplicationContext) =
        emptyList<ViewManager<*, *>>()
}