package com.poprigun4ik99.alarm_data.repositories

import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.alarm_data.room.datasources.AlarmDao
import com.poprigun4ik99.alarm_data.room.mappers.toData
import com.poprigun4ik99.alarm_data.room.mappers.toDomain
import com.poprigun4ik99.domain.dispatcher.DefaultDispatcherProvider
import com.poprigun4ik99.domain.dispatcher.DispatcherProvider
import com.poprigun4ik99.domain.repositories.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AlarmRepositoryImplementation(private val alarmDao: AlarmDao): AlarmRepository {

    override suspend fun insertAlarmRecord(record: AlarmRecord): Long {
        return withContext(DefaultDispatcherProvider.io()) {
            alarmDao.insertAlarmRecord(record.toData())
        }
    }

    override suspend fun deleteAllAlarmRecords() {
        withContext(DefaultDispatcherProvider.io()) {
            alarmDao.deleteAllAlarmRecords()
        }
    }

    override fun observeAllAlarmRecords(): Flow<List<AlarmRecord>> {
        return alarmDao.observeAllAlarmRecords().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getAllAlarmRecords(): List<AlarmRecord> {
        return withContext(DefaultDispatcherProvider.io()) {
            alarmDao.getAllAlarmRecords().map { it.toDomain() }
        }
    }

    override suspend fun deleteAlarmsRecordsByIds(idList: List<Long>) {
        alarmDao.deleteAlarmsRecordsByIds(idList)
    }
}