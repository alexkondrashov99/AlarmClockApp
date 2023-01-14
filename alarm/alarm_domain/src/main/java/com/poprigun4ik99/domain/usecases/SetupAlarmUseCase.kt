package com.poprigun4ik99.domain.usecases

import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.repositories.AlarmRepository

class SetupAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val alarmSetupDelegate: AlarmSetupDelegate
) {
    suspend fun execute(timeStamp: Long) {
        setupAlarm(timeStamp)
    }

    private suspend fun setupAlarm(timeStamp: Long) {
        val alarmId = saveAlarmToDatabase(timeStamp)
        alarmSetupDelegate.setupAlarm(alarmId = alarmId.hashCode(), timeStamp)
    }

    private suspend fun saveAlarmToDatabase(timeStamp: Long): Long {
        val record = AlarmRecord(timeStamp = timeStamp, shouldVibrate = true)
        //alarmRepository.deleteAllAlarmRecords()
        return alarmRepository.insertAlarmRecord(record)
    }

}