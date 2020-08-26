package com.todorant.widget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import org.jetbrains.anko.UI
import org.jetbrains.anko.button
import org.jetbrains.anko.verticalLayout

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences =
            getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)
        setContentView(UI {
            verticalLayout {
                button("SET TOKEN") {
                    setOnClickListener {
                        preferences.edit()
                            .putString(
                                KEY_TOKEN,
                                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoi0JLQu9Cw0LQiLCJzdWJzY3JpcHRpb25TdGF0dXMiOiJ0cmlhbCIsImVtYWlsIjoidmxhZGthbHl1emhueXVAZ21haWwuY29tIiwiaWF0IjoxNTk4MTE0MjA3fQ.v94sCC191yRqouB7_o9rqyhm0b9-1fNc_uW2ZtMrx9c"
                            )
                            .apply()
                        TodorantWidget.forceUpdateAll(applicationContext)
                    }
                }
            }
        }.view)
        if (!intent.hasExtra("widget_id")) {
            finish()
        }
    }
}