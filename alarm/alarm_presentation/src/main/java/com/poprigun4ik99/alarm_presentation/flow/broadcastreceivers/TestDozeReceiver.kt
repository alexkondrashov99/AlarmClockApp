package com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.poprigun4ik99.alarm_presentation.flow.goAsync
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class TestDozeReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmSetupDelegate: AlarmSetupDelegate = get()

    override fun onReceive(context: Context, intent: Intent) = goAsync() {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        Log.d("IDLE", "CHANGED ${pm.isDeviceIdleMode}")

        if (pm.isDeviceIdleMode) {
            delay(5000)
            alarmSetupDelegate.setupAlarm(99, System.currentTimeMillis() + 5 * 1000)
        }
    }
}