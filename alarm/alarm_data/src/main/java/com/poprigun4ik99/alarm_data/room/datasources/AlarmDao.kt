package com.poprigun4ik99.alarm_data.room.datasources

import androidx.room.*
import com.poprigun4ik99.alarm_data.room.model.AlarmRecordRoom
import com.poprigun4ik99.domain.model.AlarmRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmRecord(record: AlarmRecordRoom): Long

    @Query("UPDATE alarmrecordroom SET isAlarmPassed = 1 WHERE id = :id")
    suspend fun markAlarmRecordAsPassed(id: Long)

    @Query("DELETE FROM alarmrecordroom")
    suspend fun deleteAllAlarmRecords()

    @Query("SELECT * FROM alarmrecordroom")
    fun observeAllAlarmRecords(): Flow<List<AlarmRecordRoom>>

    @Query("SELECT * FROM alarmrecordroom")
    suspend fun getAllAlarmRecords(): List<AlarmRecordRoom>

    @Query("DELETE FROM alarmrecordroom WHERE id in (:idList)")
    suspend fun deleteAlarmsRecordsByIds(idList: List<Long>)

    @Query("SELECT * FROM alarmrecordroom WHERE id = :id")
    suspend fun getAlarmRecordById(id: Long): AlarmRecord
}