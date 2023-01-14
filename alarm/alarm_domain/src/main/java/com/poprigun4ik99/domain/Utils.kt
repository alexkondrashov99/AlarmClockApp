package com.poprigun4ik99.domain

import com.poprigun4ik99.domain.model.AlarmRecord
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.hours


fun List<AlarmRecord>.getUpcomingAlarms(): List<AlarmRecord> {
    return this.filter { alarm ->
        Instant.ofEpochMilli(alarm.timeStamp)
            .atZone(ZoneId.systemDefault())
            .isBefore(ZonedDateTime.now()).not()
    }
}

fun List<AlarmRecord>.getPastAlarms(): List<AlarmRecord> {
    return this.filter { alarm ->
        Instant.ofEpochMilli(alarm.timeStamp)
            .atZone(ZoneId.systemDefault())
            .isBefore(ZonedDateTime.now())
    }
}

fun Long.toRegularTimeString(): String {
    val zonedDateTime = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())

    return "${String.format("%02d", zonedDateTime.hour)}:${String.format("%02d", zonedDateTime.minute)}"
}