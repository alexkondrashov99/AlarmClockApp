package com.poprigun4ik99.domain.delegates.alarmdelegate

interface AlarmSetupDelegate {
    fun cancelAlarm(alarmId: Long)
    fun setupAlarm(alarmId: Long, alarmTime: Long)
}