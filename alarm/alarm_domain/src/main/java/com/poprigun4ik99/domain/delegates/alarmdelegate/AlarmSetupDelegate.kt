package com.poprigun4ik99.domain.delegates.alarmdelegate

interface AlarmSetupDelegate {
    fun cancelAlarm(alarmId: Int)
    fun setupAlarm(alarmId: Int, alarmTime: Long)

    companion object {
        const val KEY_ALARM_INTENT_ID = "KEY_ALARM_INTENT_ID"
    }
}