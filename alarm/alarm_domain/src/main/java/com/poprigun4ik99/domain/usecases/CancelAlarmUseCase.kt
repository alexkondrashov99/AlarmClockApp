package com.poprigun4ik99.domain.usecases

import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import com.poprigun4ik99.domain.repositories.AlarmRepository

class CancelAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val alarmSetupDelegate: AlarmSetupDelegate,
) {
    suspend fun execute(alarmIds: List<Long>) {
        alarmRepository.deleteAlarmsRecordsByIds(alarmIds)
        alarmIds.forEach {
            alarmSetupDelegate.cancelAlarm(it.hashCode())
        }
    }
}