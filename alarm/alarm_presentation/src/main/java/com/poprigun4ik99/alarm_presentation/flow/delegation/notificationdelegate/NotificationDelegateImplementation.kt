package com.poprigun4ik99.alarm_presentation.flow.delegation.notificationdelegate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.poprigun4ik99.alarm_presentation.flow.Constants
import com.poprigun4ik99.alarm_presentation.flow.alarm.AlarmActivity
import com.poprigun4ik99.domain.delegates.notificationdelegate.NotificationDelegate

class NotificationDelegateImplementation(private val context: Context): NotificationDelegate {

    private fun getFullScreenIntent(context: Context, alarmId: Long): PendingIntent {
        val intent = Intent(context, AlarmActivity::class.java)
            .apply { putExtra(Constants.KEY_ALARM_ID, alarmId) }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE).apply {

        }
    }

    override fun showNotificationWithFullScreenIntent(alarmId: Long) {
        val piFinishAlarmActivity = PendingIntent.getBroadcast(
            context,
            0,
            Intent(AlarmActivity.INTENT_CLOSE_ALARM_ACTIVITY)
                .apply { putExtra(Constants.KEY_ALARM_ID, alarmId) },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID_ALARM)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("title")
            .setContentText("description")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(getFullScreenIntent(context, alarmId), true)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, "cancel", piFinishAlarmActivity)


        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)

        buildChannel(context)
        val notification = builder.build()
        notificationManager?.notify(alarmId.hashCode(), notification)
    }

    override fun removeNotification(alarmId: Long) {
        context.getSystemService(NotificationManager::class.java)?.cancel(alarmId.hashCode())
    }

    private fun buildChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.NOTIFICATION_ALARM_CHANNEL_NAME
            val descriptionText = Constants.NOTIFICATION_ALARM_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(Constants.CHANNEL_ID_ALARM, name, importance)
            mChannel.description = descriptionText

            val notificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(mChannel)
        }
    }
}