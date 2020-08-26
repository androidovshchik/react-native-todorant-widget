@file:Suppress("SpellCheckingInspection")

package com.todorant.widget.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TodoCurrent {

    @SerializedName("todosCount")
    @Expose
    var todosCount = 0

    @SerializedName("incompleteTodosCount")
    @Expose
    var incompleteTodosCount = 0

    @SerializedName("todo")
    @Expose
    var todo: Todo? = null

    @SerializedName("state")
    @Expose
    var state: State? = null

    @SerializedName("tags")
    @Expose
    var tags: List<Any>? = null

    @SerializedName("points")
    @Expose
    var points: Int? = null
}

class Todo {

    @SerializedName("completed")
    @Expose
    var completed: Boolean? = null

    @SerializedName("frog")
    @Expose
    var frog = false

    @SerializedName("frogFails")
    @Expose
    var frogFails: Int? = null

    @SerializedName("skipped")
    @Expose
    var skipped = false

    @SerializedName("order")
    @Expose
    var order: Int? = null

    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null

    @SerializedName("encrypted")
    @Expose
    var encrypted: Boolean? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("text")
    @Expose
    var text: String? = null

    @SerializedName("monthAndYear")
    @Expose
    var monthAndYear: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("time")
    @Expose
    var time: Any? = null

    @SerializedName("delegateAccepted")
    @Expose
    var delegateAccepted: Any? = null

    /**
     * BELOW FIELDS ARE ADDED MANUALLY
     **/

    @SerializedName("todosCount")
    @Expose
    var todosCount = 0

    @SerializedName("incompleteTodosCount")
    @Expose
    var incompleteTodosCount = 0
}

class State {

    @SerializedName("planning")
    @Expose
    var planning: Boolean? = null

    @SerializedName("subscriptionStatus")
    @Expose
    var subscriptionStatus: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("subscriptionIdExists")
    @Expose
    var subscriptionIdExists: Boolean? = null

    @SerializedName("subscriptionType")
    @Expose
    var subscriptionType: String? = null

    @SerializedName("settings")
    @Expose
    var settings: Settings? = null
}

class Settings {

    @SerializedName("preserveOrderByTime")
    @Expose
    var preserveOrderByTime: Boolean? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("duplicateTagInBreakdown")
    @Expose
    var duplicateTagInBreakdown: Boolean? = null
}