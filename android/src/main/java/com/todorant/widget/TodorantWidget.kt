package com.todorant.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.UiThread
import com.todorant.widget.api.Todo
import com.todorant.widget.extensions.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class TodorantWidget : AppWidgetProvider() {

    @UiThread
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        with(context) {
            val preferences =
                getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)
            val hasToken = !preferences.getString(KEY_TOKEN, null).isNullOrBlank()
            val todo = preferences.getString(KEY_TODO, null)?.let {
                gson.fromJson(it, Todo::class.java)
            }
            Log.v(TAG, "onUpdate $hasToken ${todo?.id}")
            for (id in ids) {
                updateWidget(id, hasToken, todo)
            }
            if (hasToken && todo == null) {
                if (hasActiveRequest.compareAndSet(false, true)) {
                    ApiService.launch(applicationContext)
                }
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        manager: AppWidgetManager,
        id: Int,
        newOptions: Bundle?
    ) {
        with(context) {
            val preferences =
                getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)
            val hasToken = !preferences.getString(KEY_TOKEN, null).isNullOrBlank()
            val todo = preferences.getString(KEY_TODO, null)?.let {
                gson.fromJson(it, Todo::class.java)
            }
            Log.v(TAG, "onAppWidgetOptionsChanged $id $hasToken ${todo?.id}")
            updateWidget(id, hasToken, todo)
        }
    }

    @UiThread
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val id = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        with(context) {
            when (val action = intent.action) {
                ACTION_DONE, ACTION_DELETE, ACTION_SKIP, ACTION_REFRESH -> {
                    if (hasActiveRequest.compareAndSet(false, true)) {
                        Log.v(TAG, "Approving onReceive $action $id")
                        forceUpdateAll(applicationContext)
                        ApiService.launch(applicationContext, "action" to action)
                    } else {
                        Log.v(TAG, "Rejecting onReceive $action $id")
                    }
                }
                ACTION_OPEN -> {
                    Log.v(TAG, "onReceive $action $id")
                    packageManager.getLaunchIntentForPackage(packageName)?.let {
                        startActivity(it.putExtra("widget_id", id).newTask())
                    }
                }
                else -> Log.v(TAG, "onReceive $action $id")
            }
        }
    }

    companion object {

        private val noPrintRegex = "\\p{C}".toRegex()

        private val Context.widget: ComponentName
            get() = ComponentName(applicationContext, TodorantWidget::class.java)

        fun toggle(context: Context, enable: Boolean) {
            context.packageManager.setComponentEnabledSetting(
                context.widget,
                if (enable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        fun forceUpdateAll(context: Context) {
            with(context) {
                val ids = appWidgetManager.getAppWidgetIds(widget)
                sendBroadcast(intentFor<TodorantWidget>().apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                })
            }
        }

        private fun Context.updateWidget(id: Int, hasToken: Boolean, todo: Todo?) {
            val options = appWidgetManager.getAppWidgetOptions(id)
            val isPortrait = resources.configuration.orientation == ORIENTATION_PORTRAIT
            val width = options.getInt(
                if (isPortrait) {
                    AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH
                } else {
                    AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH
                }
            )
            if (!hasToken || todo == null) {
                appWidgetManager.updateAppWidget(
                    id,
                    RemoteViews(packageName, R.layout.widget_message).apply {
                        setTextViewText(
                            R.id.tv_message,
                            getString(if (!hasToken) R.string.widget_login else R.string.widget_loading)
                        )
                        setOnClickPendingIntent(R.id.tv_message, getClickIntent(id, ACTION_OPEN))
                    }
                )
                return
            }
            appWidgetManager.updateAppWidget(
                id,
                RemoteViews(packageName, R.layout.widget_layout).apply {
                    val allCount = todo.todosCount
                    val doneCount = allCount - todo.incompleteTodosCount
                    setTextViewText(R.id.tv_start, doneCount.toString())
                    if (allCount == 0) {
                        setBackgroundColor(R.id.iv_lines, getColorRes(R.color.widgetLineGray))
                        setImageViewResource(R.id.iv_lines, 0)
                    } else {
                        setBackgroundColor(R.id.iv_lines, Color.TRANSPARENT)
                        setImageViewBitmap(R.id.iv_lines, drawLines(width, allCount, doneCount))
                    }
                    setTextViewText(R.id.tv_end, allCount.toString())
                    setTextViewText(
                        R.id.tv_text, todo.text?.replace("\n", "<br>")
                            ?.replace(noPrintRegex, "")
                            ?.replace("<br>", "\n")
                            ?.formatLinks()
                    )
                    setOnClickPendingIntent(R.id.ib_done, getClickIntent(id, ACTION_DONE))
                    setOnClickPendingIntent(R.id.ib_delete, getClickIntent(id, ACTION_DELETE))
                    setOnClickPendingIntent(R.id.ib_skip, getClickIntent(id, ACTION_SKIP))
                    setOnClickPendingIntent(R.id.ib_refresh, getClickIntent(id, ACTION_REFRESH))
                    setOnClickPendingIntent(R.id.ib_add, getClickIntent(id, ACTION_OPEN))
                    val enablePanel = !hasActiveRequest.get()
                    toggle(R.id.ib_done, enablePanel)
                    setImageViewResource(
                        R.id.ib_done,
                        if (enablePanel) R.drawable.wg_done else R.drawable.wg_done_disabled
                    )
                    toggle(R.id.ib_delete, enablePanel)
                    setImageViewResource(
                        R.id.ib_delete,
                        if (enablePanel) R.drawable.wg_delete else R.drawable.wg_delete_disabled
                    )
                    if (!todo.frog && !todo.skipped) {
                        setViewVisibility(R.id.ib_skip, View.VISIBLE)
                        toggle(R.id.ib_skip, enablePanel)
                        setImageViewResource(
                            R.id.ib_skip,
                            if (enablePanel) R.drawable.wg_arrow else R.drawable.wg_arrow_disabled
                        )
                    } else {
                        setViewVisibility(R.id.ib_skip, View.GONE)
                    }
                    toggle(R.id.ib_refresh, enablePanel)
                    setImageViewResource(
                        R.id.ib_refresh,
                        if (enablePanel) R.drawable.wg_refresh else R.drawable.wg_refresh_disabled
                    )
                }
            )
        }

        private fun Context.getClickIntent(widgetId: Int, action: String): PendingIntent {
            return pendingReceiverFor(intentFor<TodorantWidget>().also {
                it.action = action
                it.data = Uri.parse(it.toUri(Intent.URI_INTENT_SCHEME))
                it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }, widgetId)
        }
    }
}