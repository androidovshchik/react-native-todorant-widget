package com.todorant.widget

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.atomic.AtomicBoolean

// public keys
internal const val KEY_TOKEN = "token"
internal const val KEY_PASSWORD = "password"

// private keys
internal const val KEY_TODO = "widget_todo"

internal const val ACTION_NONE = "action_none"
internal const val ACTION_DONE = "action_done"
internal const val ACTION_DELETE = "action_delete"
internal const val ACTION_SKIP = "action_skip"
internal const val ACTION_REFRESH = "action_refresh"
internal const val ACTION_OPEN = "action_open"

internal const val TAG = "WIDGET_TAG"

@Volatile
var prefName = "wit_player_shared_preferences"
@Volatile
var appPackage = "com.todorant"

val hasActiveRequest = AtomicBoolean(false)

val gson: Gson = GsonBuilder()
    .setLenient()
    .excludeFieldsWithoutExposeAnnotation()
    .create()

val todorantApi: TodorantApi = Retrofit.Builder()
    .client(
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor { message ->
                Log.v(TAG, message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    )
    .baseUrl("https://backend.todorant.com/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
    .create(TodorantApi::class.java)