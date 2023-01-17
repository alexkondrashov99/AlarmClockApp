package com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.poprigun4ik99.alarm_presentation.flow.goAsync
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import com.poprigun4ik99.domain.getUpcomingAlarms
import com.poprigun4ik99.domain.repositories.AlarmRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RebootReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmSetupDelegate: AlarmSetupDelegate = get()
    private val alarmRepository: AlarmRepository = get()

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action && context != null) {

            val upcomingAlarms = alarmRepository.getAllAlarmRecords().getUpcomingAlarms()

            upcomingAlarms.forEach { alarm ->
                alarmSetupDelegate.setupAlarm(
                    alarmId = alarm.id,
                    alarm.timeStamp
                )

//                //TODO remove. it is for testing
//                alarmRepository.insertAlarmRecord(
//                    alarm.copy(
//                        description = "reboot setup ${
//                            ZonedDateTime.now().format(
//                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(
//                                    ZoneId.systemDefault()
//                                )
//                            )
//                        }"
//                    )
//                )
            }
        }
    }
}