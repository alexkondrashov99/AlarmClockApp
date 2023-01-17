package com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.content.ContextCompat.getSystemService
import com.poprigun4ik99.alarm_presentation.flow.Constants
import com.poprigun4ik99.alarm_presentation.flow.goAsync
import com.poprigun4ik99.alarm_presentation.flow.services.AlarmService
import com.poprigun4ik99.domain.delegates.notificationdelegate.NotificationDelegate
import com.poprigun4ik99.domain.usecases.MarkAlarmAsPassedUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationDelegate: NotificationDelegate = get()

    private val markAlarmAsPassedUseCase: MarkAlarmAsPassedUseCase = get()

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val alarmId = intent?.getLongExtra(Constants.KEY_ALARM_ID, -1L)

            if (alarmId == -1L || alarmId == null) {
                throw RuntimeException("alarm id cannot be $alarmId")
            }

            val pm = getSystemService(context, PowerManager::class.java)
            if (pm?.isInteractive == true) {
                context.startForegroundService(
                    Intent(context, AlarmService::class.java).apply {
                        intent.extras?.let {
                            this.putExtras(it)
                        }
                    }
                )
            } else {
                notificationDelegate.showNotificationWithFullScreenIntent(alarmId)
            }
            goAsync {
                markAlarmAsPassedUseCase.execute(alarmId)
            }
        }
    }
}