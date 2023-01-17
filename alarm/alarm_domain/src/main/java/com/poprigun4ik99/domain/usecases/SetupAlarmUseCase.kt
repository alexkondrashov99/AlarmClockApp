package com.poprigun4ik99.domain.usecases

import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.repositories.AlarmRepository

class SetupAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val alarmSetupDelegate: AlarmSetupDelegate
) {
    suspend fun execute(timeStamp: Long, description: String) {
        setupAlarm(timeStamp, description)
    }

    private suspend fun setupAlarm(timeStamp: Long, description: String) {
        val alarmId = saveAlarmToDatabase(timeStamp, description)
        alarmSetupDelegate.setupAlarm(alarmId = alarmId, timeStamp)
    }

    private suspend fun saveAlarmToDatabase(timeStamp: Long, description: String): Long {
        val record = AlarmRecord(timeStamp = timeStamp, description = description, isAlarmPassed = false)
        return alarmRepository.insertAlarmRecord(record)
    }
}