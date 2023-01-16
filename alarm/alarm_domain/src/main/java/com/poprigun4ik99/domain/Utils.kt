package com.poprigun4ik99.domain

import com.poprigun4ik99.domain.model.AlarmRecord
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


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

fun Long.toZoneDateTime(): ZonedDateTime {
    return  Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
}

fun Long.toRegularDateString(): String {
    val zdt = this.toZoneDateTime()
    return zdt.toRegularDateString()
}

fun ZonedDateTime.toRegularDateString(): String {
    return "${String.format("%02d", this.dayOfMonth)}.${String.format("%02d", this.monthValue)}.${String.format("%02d", this.year)}"
}

fun Long.toRegularTimeString(): String {
    val zoneDateTime = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())

    return "${String.format("%02d", zoneDateTime.hour)}:${String.format("%02d", zoneDateTime.minute)}"
}