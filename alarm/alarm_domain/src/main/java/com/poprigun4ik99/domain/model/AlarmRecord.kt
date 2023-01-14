package com.poprigun4ik99.domain.model

data class AlarmRecord(
    val id: Long = 0,
    val timeStamp: Long,
    val shouldVibrate: Boolean,
    val description: String? = null
)
