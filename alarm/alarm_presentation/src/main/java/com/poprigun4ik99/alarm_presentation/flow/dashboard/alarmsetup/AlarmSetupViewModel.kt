package com.poprigun4ik99.alarm_presentation.flow.dashboard.alarmsetup

import androidx.lifecycle.*
import com.poprigun4ik99.alarm_presentation.flow.SingleLiveEvent
import com.poprigun4ik99.domain.toRegularDateString
import com.poprigun4ik99.domain.toZoneDateTime
import com.poprigun4ik99.domain.usecases.SetupAlarmUseCase
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class AlarmSetupViewModel(
    private val setupAlarmUseCase: SetupAlarmUseCase,
) : ViewModel() {

    private val _dateLiveData = MutableLiveData<ZonedDateTime>(System.currentTimeMillis().toZoneDateTime())
    private val _timeLiveData = MutableLiveData<HoursAndMinutes>(getDefaultHoursAndMinutes())
    private val _navigateFinish = SingleLiveEvent<Unit>()
    private val _showInvalidTimeDialog = SingleLiveEvent<Unit>()

    val dateLiveData: LiveData<String> = _dateLiveData.map { it.toRegularDateString() }
    val timeLiveData: LiveData<HoursAndMinutes> = _timeLiveData
    val showInvalidTimeError: LiveData<Unit> = _showInvalidTimeDialog
    val navigateFinish: LiveData<Unit> = _navigateFinish

    private var description: String = EMPTY_DESCRIPTION

    fun setDate(date: Long) {
        _dateLiveData.value = date.toZoneDateTime()
    }

    fun setTime(hours: Int, minutes: Int) {
        _timeLiveData.value = HoursAndMinutes(hours, minutes)
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getDescription(): String {
        return description
    }

    fun onCreateAlarmClicked() {
        val hoursAndMinutes = _timeLiveData.value
        val date = _dateLiveData.value

        if (hoursAndMinutes != null && date != null) {
            val zdtAlarm = date
                .withHour(hoursAndMinutes.hours)
                .withMinute(hoursAndMinutes.minutes)
                .withSecond(0)

            if (zdtAlarm.isBefore(ZonedDateTime.now()).not()) {
                viewModelScope.launch {
                    setupAlarmUseCase.execute(zdtAlarm.toInstant().toEpochMilli())
                    //setupAlarmUseCase.execute(System.currentTimeMillis() + 10 * 1000)
                    _navigateFinish.call()
                }
            } else {
                _showInvalidTimeDialog.call()
            }
        }
    }

    private fun getDefaultHoursAndMinutes(): HoursAndMinutes {
        val now = System.currentTimeMillis().toZoneDateTime()
        return HoursAndMinutes(now.hour, now.minute)
    }

    data class HoursAndMinutes(
        val hours: Int,
        val minutes: Int
    )

    companion object {
        const val EMPTY_DESCRIPTION = ""
    }
}