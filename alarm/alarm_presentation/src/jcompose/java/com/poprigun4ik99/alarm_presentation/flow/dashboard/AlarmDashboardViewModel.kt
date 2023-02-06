package com.poprigun4ik99.alarm_presentation.flow.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.repositories.AlarmRepository
import com.poprigun4ik99.domain.usecases.CancelAlarmUseCase
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AlarmDashboardViewModel(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmUseCase: CancelAlarmUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<AlarmDashboardUiState>(
        AlarmDashboardUiState(emptyList())
    )

    val uiState = _uiState.asStateFlow()

    init {
        observeAlarmRecords()
    }

    fun navigateAddNewAlarm() {
        _uiState.update { oldState ->
            oldState.copy(navigation = triggered(Navigation.AddNewAlarm))
        }
    }

    fun consumeNavigation() {
        _uiState.update { oldState ->
            oldState.copy(navigation = consumed())
        }
    }

    fun cancelAlarm(alarmId: Long) {
        viewModelScope.launch {
            cancelAlarmUseCase.execute(listOf(alarmId))
        }
    }

    private fun observeAlarmRecords() {
        alarmRepository.observeAllAlarmRecords()
            .filterNotNull()
            .onEach { alarms ->
                _uiState.update { oldState ->
                    oldState.copy(alarmsList = alarmsListToUiList(alarms.filter { it.isAlarmPassed.not() }))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun alarmsListToUiList(list: List<AlarmRecord>): List<AlarmUiItem> {
        val uiList = list.sortedBy { it.timeStamp }.map { alarm ->
            AlarmUiItem.AlarmItem(alarm)
        }
        return mutableListOf<AlarmUiItem>(
            AlarmUiItem.AddNewAlarm,
        ).apply {
            addAll(0, uiList)
        }
    }

    sealed class AlarmUiItem {
        data class AlarmItem(val alarmRecord: AlarmRecord) : AlarmUiItem()

        object AddNewAlarm : AlarmUiItem()
    }

    sealed class Navigation {
        object AddNewAlarm : Navigation()
        data class EditAlarm(val id: Long) : Navigation()
    }

    data class AlarmDashboardUiState(
        val alarmsList: List<AlarmUiItem>,
        val navigation: StateEventWithContent<Navigation> = consumed()
    )
}