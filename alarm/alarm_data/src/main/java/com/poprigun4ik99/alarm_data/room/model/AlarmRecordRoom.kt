package com.poprigun4ik99.alarm_data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmRecordRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val timeStamp: Long,
    val shouldVibrate: Boolean,
    var description: String? = null
)
