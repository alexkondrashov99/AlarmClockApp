package com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.content.ContextCompat.getSystemService
import com.poprigun4ik99.alarm_presentation.flow.goAsync
import com.poprigun4ik99.alarm_presentation.flow.services.AlarmService
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate.Companion.KEY_ALARM_INTENT_ID
import com.poprigun4ik99.domain.delegates.notificationdelegate.NotificationDelegate
import com.poprigun4ik99.domain.usecases.RemoveOldAlarmUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationDelegate: NotificationDelegate = get()

    private val removeOldAlarmUseCase: RemoveOldAlarmUseCase = get()

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val pm = getSystemService(context, PowerManager::class.java)
            if (pm?.isInteractive == true) {
                context.startForegroundService(
                    Intent(context, AlarmService::class.java).apply {
                        intent?.extras?.let {
                            this.putExtras(it)
                        }
                    }
                )
            } else {
                val alarmId = intent?.getIntExtra(KEY_ALARM_INTENT_ID, -1)

                if (alarmId == -1 || alarmId == null) {
                    throw RuntimeException("alarm id cannot be $alarmId")
                }
                notificationDelegate.showNotificationWithFullScreenIntent(alarmId)
            }
            goAsync {
                removeOldAlarmUseCase.execute()
            }
        }
    }
}