package com.poprigun4ik99.domain.delegates.notificationdelegate

interface NotificationDelegate {
    fun showNotificationWithFullScreenIntent(alarmId: Long)
    fun removeNotification(alarmId: Long)
}