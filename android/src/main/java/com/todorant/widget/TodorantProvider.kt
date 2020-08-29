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
import com.todorant.widget.extensions.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class TodorantProvider : AppWidgetProvider() {

    @UiThread
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        with(context) {
            val preferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val hasToken = !preferences.getString(KEY_TOKEN, null).isNullOrBlank()
            val todo = preferences.getString(KEY_TODO, null)?.let {
                gson.fromJson(it, Todo::class.java)
            }
            Log.v(TAG, "onUpdate $hasToken ${todo?.id}")
            for (id in ids) {
                updateWidget(id, hasToken, todo)
            }
        }
    }

    @UiThread
    override fun onAppWidgetOptionsChanged(
        context: Context,
        manager: AppWidgetManager,
        id: Int,
        newOptions: Bundle?
    ) {
        onUpdate(context, manager, intArrayOf(id))
    }

    @UiThread
    override fun onReceive(context: Context, intent: Intent) {
        with(context) {
            when (val action = intent.action) {
                ACTION_DONE, ACTION_DELETE, ACTION_SKIP, ACTION_REFRESH -> {
                    Log.v(TAG, "onReceive $action")
                    if (hasActiveRequest.compareAndSet(false, true)) {
                        val ids = appWidgetManager.getAppWidgetIds(widget)
                        if (ids.isNotEmpty()) {
                            onUpdate(applicationContext, appWidgetManager, ids)
                        }
                        ApiService.launch(applicationContext, "action" to action)
                    }
                }
                ACTION_OPEN -> {
                    Log.v(TAG, "onReceive $action")
                    packageManager.getLaunchIntentForPackage(packageName)?.let {
                        startActivity(it.putExtra("widget", true).newTask())
                    }
                }
                AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                    val force = intent.getBooleanExtra("force", true)
                    Log.v(TAG, "onReceive APPWIDGET_UPDATE $force")
                    if (force && hasActiveRequest.compareAndSet(false, true)) {
                        super.onReceive(context, intent)
                        ApiService.launch(applicationContext)
                        return
                    }
                }
                else -> Log.v(TAG, "onReceive $action")
            }
            super.onReceive(context, intent)
        }
    }

    companion object {

        private val noPrintRegex = "\\p{C}".toRegex()

        private val Context.widget: ComponentName
            get() = ComponentName(applicationContext, TodorantProvider::class.java)

        fun toggle(context: Context, enable: Boolean) {
            context.packageManager.setComponentEnabledSetting(
                context.widget,
                if (enable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        /**
         * @param force if true also make http requests besides ui updates
         */
        fun updateAll(context: Context, force: Boolean = false) {
            with(context) {
                val ids = appWidgetManager.getAppWidgetIds(widget)
                if (ids.isNotEmpty()) {
                    sendBroadcast(intentFor<TodorantProvider>().apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                        putExtra("force", force)
                    })
                }
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
            Log.v(TAG, "updateWidget $isPortrait")
            if (!hasToken) {
                appWidgetManager.updateAppWidget(
                    id,
                    RemoteViews(packageName, R.layout.widget_message).apply {
                        setTextViewText(R.id.tv_message, getString(R.string.widget_login))
                        setOnClickPendingIntent(R.id.tv_message, getClickIntent(id, ACTION_OPEN))
                    }
                )
                return
            }
            appWidgetManager.updateAppWidget(
                id,
                RemoteViews(packageName, R.layout.widget_layout).apply {
                    val allCount = todo?.todosCount ?: 0
                    val doneCount = allCount - (todo?.incompleteTodosCount ?: 0)
                    setTextViewText(R.id.tv_start, doneCount.toString())
                    if (allCount <= 1) {
                        setBackgroundColor(
                            R.id.iv_lines,
                            getColorRes(if (doneCount <= 0) R.color.widgetLineGray else R.color.widgetLineRed)
                        )
                        setImageViewResource(R.id.iv_lines, 0)
                    } else {
                        setBackgroundColor(R.id.iv_lines, Color.TRANSPARENT)
                        setImageViewBitmap(R.id.iv_lines, drawLines(width, allCount, doneCount))
                    }
                    setTextViewText(R.id.tv_end, allCount.toString())
                    setTextViewText(
                        R.id.tv_text, if (todo != null) {
                            todo.text?.replace("\n", "<br>")
                                ?.replace(noPrintRegex, "")
                                ?.replace("<br>", "\n")
                                ?.formatLinks()
                        } else {
                            getString(R.string.widget_loading)
                        }
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
                    if (todo == null || !todo.frog && !todo.skipped) {
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
            return pendingReceiverFor(intentFor<TodorantProvider>().also {
                it.action = action
                it.data = Uri.parse(it.toUri(Intent.URI_INTENT_SCHEME))
                it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }, widgetId)
        }
    }
}