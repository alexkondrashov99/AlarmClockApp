package com.poprigun4ik99.alarm_presentation.koin

import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardViewModel
import com.poprigun4ik99.alarm_presentation.flow.dashboard.alarmsetup.AlarmSetupViewModel
import com.poprigun4ik99.domain.usecases.RemoveOldAlarmUseCase
import com.poprigun4ik99.domain.usecases.SetupAlarmUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel {
        AlarmDashboardViewModel(get(), get(), get(), get())
    }

    viewModel {
        AlarmSetupViewModel(get())
    }
}