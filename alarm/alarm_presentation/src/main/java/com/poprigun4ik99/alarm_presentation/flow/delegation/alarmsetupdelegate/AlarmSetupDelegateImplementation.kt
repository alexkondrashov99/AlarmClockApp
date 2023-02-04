package com.poprigun4ik99.alarm_presentation.flow.delegation.alarmsetupdelegate

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.poprigun4ik99.alarm_presentation.flow.Constants
import com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers.AlarmReceiver
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardActivity
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate


class AlarmSetupDelegateImplementation(private val context: Context) : AlarmSetupDelegate {

    override fun setupAlarm(alarmId: Long, alarmTime: Long) {
        cancelAlarm(alarmId)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.let {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    alarmTime,
                    getInfoPendingIntent()
                ), getAlarmPendingIntent(context, alarmId)
            )
        }
    }

    override fun cancelAlarm(alarmId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.let {
            alarmManager.cancel(
                getAlarmPendingIntent(
                    context,
                    alarmId = alarmId,
                )
            )
        }
    }

    private fun getInfoPendingIntent(): PendingIntent? {
        val alarmInfoIntent = Intent(context, AlarmDashboardActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            context,
            0,
            alarmInfoIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getAlarmPendingIntent(
        context: Context,
        alarmId: Long,
    ): PendingIntent? {
        val receiverIntent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(Constants.KEY_ALARM_ID, alarmId)
            }

        return PendingIntent.getBroadcast(
            context,
            alarmId.hashCode(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}