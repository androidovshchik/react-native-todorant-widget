package com.todorant.widget

import retrofit2.Call
import retrofit2.http.*
import java.text.SimpleDateFormat
import java.util.*

interface TodorantApi {

    @GET("todo/current")
    fun todoCurrent(
        @Header("token") token: String,
        @Query("date") date: String = dateFormat.format(System.currentTimeMillis())
    ): Call<TodoCurrent>

    @PUT("todo/{id}/done")
    fun todoDone(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("todo/{id}")
    fun todoDelete(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<Unit>

    @PUT("todo/{id}/skip")
    fun todoSkip(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<Unit>

    companion object {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }
}