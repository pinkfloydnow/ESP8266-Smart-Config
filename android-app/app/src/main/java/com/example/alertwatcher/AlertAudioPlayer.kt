package com.example.alertwatcher

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class AlertAudioPlayer(private val context: Context) {

    fun playTone(toneUri: String, repetitions: Int) {
        val uri = Uri.parse(toneUri)
        repeat(repetitions.coerceAtLeast(1)) {
            MediaPlayer.create(context, uri)?.apply {
                setOnCompletionListener { mp -> mp.release() }
                start()
            }
        }
    }
}
