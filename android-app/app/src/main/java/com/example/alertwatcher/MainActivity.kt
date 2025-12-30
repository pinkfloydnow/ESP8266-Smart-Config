package com.example.alertwatcher

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.alertwatcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: AlertPreferences
    private lateinit var scheduler: AlertScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = AlertPreferences(this)
        scheduler = AlertScheduler(this)

        binding.notificationSwitch.isChecked = AlertNotificationListenerService.isEnabled(this)
        binding.statusText.text = statusText(binding.notificationSwitch.isChecked)

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !AlertNotificationListenerService.isEnabled(this)) {
                promptNotificationAccess()
            }
            binding.statusText.text = statusText(isChecked)
        }

        binding.saveButton.setOnClickListener {
            val config = AlertConfig(
                targetPackage = binding.appPackageInput.text.toString().ifBlank { null },
                keyword = binding.keywordInput.text.toString(),
                toneUri = binding.toneInput.text.toString(),
                repetitions = binding.repeatInput.text.toString().toIntOrNull() ?: 1
            )
            preferences.save(config)
            scheduler.refreshSchedule()
            binding.statusText.text = statusText(AlertNotificationListenerService.isEnabled(this))
        }

        loadExistingConfig()
    }

    private fun loadExistingConfig() {
        val config = preferences.load()
        binding.appPackageInput.setText(config.targetPackage ?: "")
        binding.keywordInput.setText(config.keyword)
        binding.toneInput.setText(config.toneUri)
        binding.repeatInput.setText(config.repetitions.toString())
    }

    private fun promptNotificationAccess() {
        AlertDialog.Builder(this)
            .setMessage(R.string.notification_permission_rationale)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun statusText(enabled: Boolean): String =
        if (enabled) getString(R.string.status_enabled) else getString(R.string.status_disabled)
}
