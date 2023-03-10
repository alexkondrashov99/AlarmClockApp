package com.poprigun4ik99.alarm_presentation.flow.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.poprigun4ik99.alarm_presentation.flow.Constants
import com.poprigun4ik99.alarm_presentation.flow.alarm.AlarmServiceActivity
import com.poprigun4ik99.domain.delegates.alarmringtonedelegate.AlarmRingtoneDelegate
import com.poprigun4ik99.domain.dispatcher.DefaultDispatcherProvider
import com.poprigun4ik99.domain.dispatcher.DispatcherProvider
import com.poprigun4ik99.domain.repositories.AlarmRepository
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.*
import java.util.concurrent.TimeUnit


class AlarmService: Service(), KoinComponent {

    private val alarmRingtoneDelegate: AlarmRingtoneDelegate = get()
    private val alarmRepository: AlarmRepository = get()

    var cancelTimer: Timer? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(DefaultDispatcherProvider.main() + job)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_SERVICE) {
            stopSelf()
        }

        val alarmId = intent?.getLongExtra(Constants.KEY_ALARM_ID, - 1L) ?: -1L

        if (alarmId == -1L) {
            stopSelf()
        }

        scope.launch {
            val alarmRecord = alarmRepository.getAlarmById(alarmId)

            buildServiceNotification(notificationId = alarmId.hashCode(), alarmRecord.description)

            alarmRingtoneDelegate.playAlarmRingtone()
            setupAlarmCancelTimer()
        }

        return START_NOT_STICKY
    }

    private fun setupAlarmCancelTimer() {
        cancelTimer = Timer()
        cancelTimer?.schedule(object : TimerTask() {
            override fun run() {
                stopSelf()
            }
        }, TimeUnit.SECONDS.toMillis(Constants.ALARM_DURATION))
    }

    private fun buildServiceNotification(notificationId: Int, description: String) {
        val notificationIntent = Intent(this, AlarmServiceActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0 or PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val stopSelf = Intent(this, AlarmService::class.java)
        stopSelf.action = ACTION_STOP_SERVICE
        val pStopSelf = PendingIntent.getService(this, 0, stopSelf, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID_ALARM)
            .setContentTitle("ALARM!!!!")
            .setContentText(description)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_delete, "Stop", pStopSelf)
            .build()

        startForeground(notificationId, notification)
    }

    private fun createChannel() {
        val name = Constants.NOTIFICATION_ALARM_CHANNEL_NAME
        val descriptionText = Constants.NOTIFICATION_ALARM_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(Constants.CHANNEL_ID_ALARM, name, importance)
        mChannel.description = descriptionText

        val notificationManager =
            ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(mChannel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmRingtoneDelegate.stopAlarmRingtone()
        cancelTimer?.cancel()
        closeServiceActivity()

        if (job.isActive) {
            job.cancel()
        }
    }

    private fun closeServiceActivity() {
        val closeAlarmServiceActivityIntent = Intent(AlarmServiceActivity.INTENT_CLOSE_ALARM_SERVICE_ACTIVITY)
        this@AlarmService.sendBroadcast(closeAlarmServiceActivityIntent)
    }

    companion object {
        var ACTION_STOP_SERVICE = "STOP"
    }
}