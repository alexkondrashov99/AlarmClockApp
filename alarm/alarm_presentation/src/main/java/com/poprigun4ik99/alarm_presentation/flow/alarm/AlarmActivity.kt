package com.poprigun4ik99.alarm_presentation.flow.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.poprigun4ik99.alarm_presentation.R
import com.poprigun4ik99.alarm_presentation.flow.Constants
import com.poprigun4ik99.alarm_presentation.flow.turnScreenOffAndKeyguardOn
import com.poprigun4ik99.alarm_presentation.flow.turnScreenOnAndKeyguardOff
import com.poprigun4ik99.domain.delegates.alarmringtonedelegate.AlarmRingtoneDelegate
import com.poprigun4ik99.domain.delegates.notificationdelegate.NotificationDelegate
import com.poprigun4ik99.domain.delegates.notificationdelegate.NotificationDelegate.Companion.KEY_ALARM_NOTIFICATION_ID
import org.koin.android.ext.android.get
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmActivity : AppCompatActivity() {

    private val alarmRingtoneDelegate: AlarmRingtoneDelegate = get()
    private var notificationDelegate: NotificationDelegate = get()

    private var finishReceiver: BroadcastReceiver? = null
    private var cancelTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        turnScreenOnAndKeyguardOff()

        //alarmRingtoneDelegate?.playAlarmRingtone()
        alarmRingtoneDelegate.playAlarmRingtone()

        findViewById<Button>(R.id.btStopAlarm).setOnClickListener {
            finishAlarm()
        }

        setupBroadCastReceiver()

        cancelTimer = Timer()
        cancelTimer?.schedule(object : TimerTask() {
            override fun run() {
                finishAlarm()
            }
        }, TimeUnit.SECONDS.toMillis(Constants.ALARM_DURATION))
    }

    private fun setupBroadCastReceiver() {
        finishReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                finishAlarm()
            }
        }

        registerReceiver(finishReceiver, IntentFilter(INTENT_CLOSE_ALARM_ACTIVITY))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAlarm()
    }

    private fun finishAlarm() {
        Log.d("NOTI", "finish alarm")
        val notificationId = intent.getIntExtra(KEY_ALARM_NOTIFICATION_ID, -1)
        if (notificationId == -1 || notificationId == null) {
            throw RuntimeException("notificationId id cannot be $notificationId")
        }

        alarmRingtoneDelegate.stopAlarmRingtone()
        notificationDelegate.removeNotification(notificationId)
        finish()
    }

    override fun onDestroy() {
        cancelTimer?.cancel()
        finishReceiver?.let { unregisterReceiver(it) }
        alarmRingtoneDelegate.stopAlarmRingtone()
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }

    companion object {
        const val INTENT_CLOSE_ALARM_ACTIVITY = "INTENT_CLOSE_ALARM_ACTIVITY"
    }
}