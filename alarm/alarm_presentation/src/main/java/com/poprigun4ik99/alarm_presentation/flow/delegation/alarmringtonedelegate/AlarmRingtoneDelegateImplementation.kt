package com.poprigun4ik99.alarm_presentation.flow.delegation.alarmringtonedelegate

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.poprigun4ik99.domain.delegates.alarmringtonedelegate.AlarmRingtoneDelegate
import java.util.*


class AlarmRingtoneDelegateImplementation(private val context: Context) : AlarmRingtoneDelegate {

    var alarmTimer: Timer? = null
    var ringtone: Ringtone? = null
    var vibrator: Vibrator? = null

    var player: MediaPlayer? = null

    override fun playAlarmRingtone() {
        if (alarmTimer == null) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)?.let { uri ->
                alarmTimer = Timer()
                alarmTimer?.scheduleAtFixedRate(object : TimerTask() {
                    init {
                        ringtone = RingtoneManager.getRingtone(context, uri)
                        vibrator = context.getSystemService(Vibrator::class.java)


                        vibrator?.takeIf { it.hasVibrator() }?.run {
                            val pattern = longArrayOf(0, 800, 800)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrate(VibrationEffect.createWaveform(pattern, 0))
                            } else {
                                vibrate(pattern, 0)
                            }
                        }
                    }

                    override fun run() {
                        if (ringtone?.isPlaying == false) {
                            ringtone?.play()
                        }
                    }

                }, 1000, 100)
            }
        }
    }

    override fun stopAlarmRingtone() {
        ringtone?.stop()
        player?.stop()
        alarmTimer?.cancel()
        vibrator?.cancel()
        alarmTimer = null
        vibrator = null
    }
}