package com.poprigun4ik99.freealarmclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, AlarmDashboardActivity::class.java))
    }
}