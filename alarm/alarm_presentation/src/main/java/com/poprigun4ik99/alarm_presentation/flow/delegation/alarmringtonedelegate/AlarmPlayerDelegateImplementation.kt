package com.poprigun4ik99.alarm_presentation.flow.delegation.alarmringtonedelegate

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.poprigun4ik99.domain.delegates.alarmringtonedelegate.AlarmRingtoneDelegate

class AlarmPlayerDelegateImplementation(private val context: Context) : AlarmRingtoneDelegate {

    var vibrator: Vibrator? = null
    var player: MediaPlayer? = null

    override fun playAlarmRingtone() {
        if (player?.isPlaying != true) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)?.let { uri ->

                player = MediaPlayer().apply {
                    (context.getSystemService(AUDIO_SERVICE) as? AudioManager)?.let { audioManager ->
                        val volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
                        this.setAudioStreamType(AudioManager.STREAM_ALARM)
                        this.setVolume(volumeLevel.toFloat(), volumeLevel.toFloat())
                        this.setDataSource(context, uri)
                        this.setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
                        isLooping = true
                    }
                }

                vibrator = context.getSystemService(Vibrator::class.java)


                vibrator?.takeIf { it.hasVibrator() }?.run {
                    val pattern = longArrayOf(0, 800, 800)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrate(VibrationEffect.createWaveform(pattern, 0))
                    } else {
                        vibrate(pattern, 0)
                    }
                }

                player?.prepare();
                player?.start()

            }
        }
    }

    override fun stopAlarmRingtone() {
        player?.stop()
        vibrator?.cancel()
        vibrator = null
    }
}