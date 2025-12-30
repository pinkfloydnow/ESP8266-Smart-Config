package com.example.alertwatcher

import android.content.Context
import java.util.Calendar

class AlertScheduler(private val context: Context) {

    private val preferences = AlertPreferences(context)

    fun refreshSchedule() {
        val config = preferences.load()
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val active = hour in config.startHour..config.endHour
        AlertNotificationListenerService.setActiveWindow(active)
    }
}
