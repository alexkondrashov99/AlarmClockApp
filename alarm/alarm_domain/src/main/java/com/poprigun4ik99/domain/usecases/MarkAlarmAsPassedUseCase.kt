package com.poprigun4ik99.domain.usecases

import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.repositories.AlarmRepository

class MarkAlarmAsPassedUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend fun execute(id: Long) {
       return alarmRepository.markAlarmAsPassed(id)
    }
}