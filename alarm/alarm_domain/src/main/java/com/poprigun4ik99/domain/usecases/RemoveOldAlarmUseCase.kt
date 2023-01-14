package com.poprigun4ik99.domain.usecases

import com.poprigun4ik99.domain.getPastAlarms
import com.poprigun4ik99.domain.getUpcomingAlarms
import com.poprigun4ik99.domain.repositories.AlarmRepository

class RemoveOldAlarmUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend fun execute() {
        alarmRepository.getAllAlarmRecords().getPastAlarms()
            .takeIf { it.isNotEmpty() }
            ?.let { upcomingAlarms ->
                alarmRepository.deleteAlarmsRecordsByIds(upcomingAlarms.map { it.id })
            }
    }
}