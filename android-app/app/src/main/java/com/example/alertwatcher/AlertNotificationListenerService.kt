package com.example.alertwatcher

import android.annotation.SuppressLint
import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class AlertNotificationListenerService : NotificationListenerService() {

    private val preferences by lazy { AlertPreferences(this) }
    private val audioPlayer by lazy { AlertAudioPlayer(this) }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!activeWindow) return
        val config = preferences.load()
        if (config.targetPackage != null && config.targetPackage != sbn.packageName) return

        val text = sbn.notification.extras
            ?.getCharSequence(Notification.EXTRA_TEXT)
            ?.toString()
            ?.lowercase() ?: return

        if (text.contains(config.keyword.lowercase())) {
            Log.d(TAG, "Keyword detected, playing tone")
            audioPlayer.playTone(config.toneUri, config.repetitions)
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        activeWindow = true
    }

    companion object {
        private const val TAG = "AlertWatcher"

        @SuppressLint("StaticFieldLeak")
        private var activeWindow: Boolean = true

        fun setActiveWindow(active: Boolean) {
            activeWindow = active
        }

        fun isEnabled(context: android.content.Context): Boolean {
            val enabledListeners = android.provider.Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            ) ?: return false
            return enabledListeners.contains(context.packageName)
        }
    }
}
