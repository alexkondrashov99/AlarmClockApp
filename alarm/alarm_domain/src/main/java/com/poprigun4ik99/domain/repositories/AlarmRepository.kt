package com.poprigun4ik99.domain.repositories

import com.poprigun4ik99.domain.model.AlarmRecord
import kotlinx.coroutines.flow.Flow


interface AlarmRepository {
    suspend fun insertAlarmRecord(record: AlarmRecord): Long
    suspend fun markAlarmAsPassed(id: Long)
    suspend fun deleteAllAlarmRecords()
    fun observeAllAlarmRecords(): Flow<List<AlarmRecord>>
    suspend fun getAllAlarmRecords(): List<AlarmRecord>
    suspend fun getAlarmById(id: Long): AlarmRecord
    suspend fun deleteAlarmsRecordsByIds(idList: List<Long>)
}