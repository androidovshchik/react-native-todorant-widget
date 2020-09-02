package com.todorant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.JobIntentService
import org.jetbrains.anko.intentFor

class ApiService : JobIntentService() {

    @WorkerThread
    @SuppressLint("ApplySharedPref")
    override fun onHandleWork(intent: Intent) {
        val preferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        try {
            val token = preferences.getString(KEY_TOKEN, null)
            require(!token.isNullOrBlank())
            val todoId = preferences.getString(KEY_TODO, null)?.let {
                gson.fromJson(it, Todo::class.java).id
            }
            if (!todoId.isNullOrBlank()) {
                when (intent.getStringExtra("action")) {
                    ACTION_DONE -> {
                        todorantApi.todoDone(token, todoId).execute()
                    }
                    ACTION_DELETE -> {
                        todorantApi.todoDelete(token, todoId).execute()
                    }
                    ACTION_SKIP -> {
                        todorantApi.todoSkip(token, todoId).execute()
                    }
                }
            }
            val current = todorantApi.todoCurrent(token).execute().body()!!
            val todo = (current.todo ?: Todo()).apply {
                todosCount = current.todosCount
                incompleteTodosCount = current.incompleteTodosCount
                if (!id.isNullOrBlank()) {
                    if (encrypted == true) {
                        val password = preferences.getString(KEY_PASSWORD, null)
                        if (!password.isNullOrBlank()) {
                            text = try {
                                CryptoAES.decrypt(password, text.orEmpty())
                            } catch (e: Throwable) {
                                Log.e(TAG, e.message, e)
                                "Invalid decryption password"
                            }
                        }
                    }
                    time?.let {
                        text = "$it ${text.orEmpty()}"
                    }
                    if (frog) {
                        text = "\uD83D\uDC38 ${text.orEmpty()}"
                    }
                } else {
                    text = if (todosCount > 0) {
                        "\uD83C\uDF89\nCongratulations!\n\uD83D\uDCAA You did it! All the tasks for today are done, go get rest or maybe dance a little \uD83D\uDC83"
                    } else {
                        "\uD83D\uDC1D\nTo infinity!\nYou don't have any todos for today. If you want to work — add a new todo for today or take the todos from future days."
                    }
                }
            }
            preferences.edit()
                .putString(KEY_TODO, gson.toJson(todo))
                .commit()
        } catch (ignored: IllegalArgumentException) {
        } catch (e: Throwable) {
            Log.e(TAG, e.message, e)
        } finally {
            hasActiveRequest.set(false)
            TodorantProvider.updateAll(applicationContext, false)
        }
    }

    companion object {

        fun launch(context: Context, vararg params: Pair<String, Any?>) {
            with(context) {
                enqueueWork(
                    applicationContext,
                    ApiService::class.java,
                    100,
                    intentFor<ApiService>(*params)
                )
            }
        }
    }
}