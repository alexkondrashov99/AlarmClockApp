package com.poprigun4ik99.alarm_presentation.flow.alarm

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.poprigun4ik99.alarm_presentation.R
import com.poprigun4ik99.alarm_presentation.flow.services.AlarmService
import com.poprigun4ik99.alarm_presentation.flow.turnScreenOffAndKeyguardOn
import com.poprigun4ik99.alarm_presentation.flow.turnScreenOnAndKeyguardOff


class AlarmServiceActivity : AppCompatActivity() {

    var finishReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        turnScreenOnAndKeyguardOff()

        findViewById<Button>(R.id.btStopAlarm).setOnClickListener {
            finishAlarm()
        }

        setupBroadCastReceiver()
    }

    private fun setupBroadCastReceiver() {
        finishReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                finishAlarm()
            }
        }

        registerReceiver(finishReceiver, IntentFilter(INTENT_CLOSE_ALARM_SERVICE_ACTIVITY))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAlarm()
    }

    private fun finishAlarm() {
        val stopAlarmService = Intent(this, AlarmService::class.java)
        stopAlarmService.action = AlarmService.ACTION_STOP_SERVICE
        stopService(stopAlarmService)
        finish()
    }

    override fun onDestroy() {
        finishReceiver?.let { unregisterReceiver(it) }
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
        //Remove app from task manager
        if (am != null) {
            val tasks = am.appTasks
            if (tasks != null && tasks.size > 0) {
                tasks[0].setExcludeFromRecents(true)
            }
        }
    }

    companion object {
        const val INTENT_CLOSE_ALARM_SERVICE_ACTIVITY = "INTENT_CLOSE_ALARM_SERVICE_ACTIVITY"
    }
}