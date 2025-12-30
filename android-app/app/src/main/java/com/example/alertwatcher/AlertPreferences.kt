package com.example.alertwatcher

import android.content.Context
import androidx.core.content.edit

class AlertPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("alert_watcher", Context.MODE_PRIVATE)

    fun save(config: AlertConfig) {
        prefs.edit {
            putString(KEY_PACKAGE, config.targetPackage)
            putString(KEY_KEYWORD, config.keyword)
            putString(KEY_TONE_URI, config.toneUri)
            putInt(KEY_REPETITIONS, config.repetitions)
            putInt(KEY_START_HOUR, config.startHour)
            putInt(KEY_END_HOUR, config.endHour)
        }
    }

    fun load(): AlertConfig = AlertConfig(
        targetPackage = prefs.getString(KEY_PACKAGE, null),
        keyword = prefs.getString(KEY_KEYWORD, "alert dvr") ?: "alert dvr",
        toneUri = prefs.getString(KEY_TONE_URI, "content://settings/system/notification_sound")
            ?: "content://settings/system/notification_sound",
        repetitions = prefs.getInt(KEY_REPETITIONS, 1),
        startHour = prefs.getInt(KEY_START_HOUR, 0),
        endHour = prefs.getInt(KEY_END_HOUR, 23),
    )

    companion object {
        private const val KEY_PACKAGE = "package"
        private const val KEY_KEYWORD = "keyword"
        private const val KEY_TONE_URI = "toneUri"
        private const val KEY_REPETITIONS = "repetitions"
        private const val KEY_START_HOUR = "startHour"
        private const val KEY_END_HOUR = "endHour"
    }
}
