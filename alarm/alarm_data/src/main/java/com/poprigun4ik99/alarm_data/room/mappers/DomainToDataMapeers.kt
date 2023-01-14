package com.poprigun4ik99.alarm_data.room.mappers

import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.alarm_data.room.model.AlarmRecordRoom

fun AlarmRecord.toData(): AlarmRecordRoom {
    return AlarmRecordRoom(id, timeStamp, shouldVibrate, description)
}