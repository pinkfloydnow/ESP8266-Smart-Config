package com.example.alertwatcher

data class AlertConfig(
    val targetPackage: String?,
    val keyword: String,
    val toneUri: String,
    val repetitions: Int,
    val startHour: Int = 0,
    val endHour: Int = 23,
)
