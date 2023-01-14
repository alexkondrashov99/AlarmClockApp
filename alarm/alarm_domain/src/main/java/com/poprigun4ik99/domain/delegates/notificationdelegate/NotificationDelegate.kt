package com.poprigun4ik99.domain.delegates.notificationdelegate

interface NotificationDelegate {
    fun showNotificationWithFullScreenIntent(alarmId: Int)
    fun removeNotification(alarmId: Int)

    companion object {
        const val KEY_ALARM_NOTIFICATION_ID = "KEY_ALARM_NOTIFICATION_ID"
    }
}