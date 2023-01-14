package com.poprigun4ik99.alarm_presentation.flow.delegation.alarmsetupdelegate

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers.AlarmReceiver
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardActivity
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate.Companion.KEY_ALARM_INTENT_ID

class AlarmSetupDelegateImplementation(private val context: Context) : AlarmSetupDelegate {

    override fun setupAlarm(alarmId: Int, alarmTime: Long) {
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

    override fun cancelAlarm(alarmId: Int) {
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
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getAlarmPendingIntent(
        context: Context,
        alarmId: Int,
    ): PendingIntent? {
        val receiverIntent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(KEY_ALARM_INTENT_ID, alarmId)
            }

        return PendingIntent.getBroadcast(
            context,
            alarmId,
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}