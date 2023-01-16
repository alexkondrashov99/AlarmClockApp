package com.poprigun4ik99.freealarmclock

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.core.content.ContextCompat
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, AlarmDashboardActivity::class.java))
    }
}