package com.poprigun4ik99.alarm_presentation.flow.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.repositories.AlarmRepository
import com.poprigun4ik99.domain.usecases.CancelAlarmUseCase
import com.poprigun4ik99.domain.usecases.RemoveOldAlarmUseCase
import com.poprigun4ik99.domain.usecases.SetupAlarmUseCase
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AlarmDashboardViewModel(
    private val alarmRepository: AlarmRepository,
    private val setupAlarmUseCase: SetupAlarmUseCase,
    private val removeOldAlarmUseCase: RemoveOldAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase
) : ViewModel() {

    val alarmRecordsLiveData = MutableLiveData<List<AlarmUiItem>>()

    init {
        //clearPastAlarmsFromDatabase()
    }

    fun cancelAlarm(alarmId: Long) {
        viewModelScope.launch {
            cancelAlarmUseCase.execute(listOf(alarmId))
        }
    }

    fun setupAlarm(time: Long) {
        viewModelScope.launch {
            setupAlarmUseCase.execute(time)
        }
    }

    fun observeAlarmRecords() {
        alarmRepository.observeAllAlarmRecords()
            .filterNotNull()
            .onEach {
                alarmRecordsLiveData.value = alarmsListToUiList(it)
            }
            .launchIn(viewModelScope)
    }

    private fun alarmsListToUiList(list: List<AlarmRecord>): List<AlarmUiItem> {
        val uiList = list.sortedBy { it.timeStamp }.mapIndexed { i, alarm ->
            when (i) {
                0 -> AlarmUiItem.AlarmItem(alarm, topDividerIsVisible = false, bottomDividerIsVisible = true)
                else -> AlarmUiItem.AlarmItem(alarm, topDividerIsVisible = true, bottomDividerIsVisible = true)
            }
        }
        return mutableListOf<AlarmUiItem>(
            AlarmUiItem.AddNewAlarm,
            ).apply {
            addAll(0, uiList)
        }
    }

    private fun clearPastAlarmsFromDatabase() {
        viewModelScope.launch {
            removeOldAlarmUseCase.execute()
        }
    }

    sealed class AlarmUiItem {
        data class AlarmItem(
            val alarmRecord: AlarmRecord,
            val topDividerIsVisible: Boolean,
            val bottomDividerIsVisible: Boolean
        ) : AlarmUiItem()

        object AddNewAlarm : AlarmUiItem()
    }
}