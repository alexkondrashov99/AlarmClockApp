package com.poprigun4ik99.alarm_presentation.koin

import com.poprigun4ik99.alarm_data.repositories.AlarmRepositoryImplementation
import com.poprigun4ik99.alarm_data.room.AppDataBase
import com.poprigun4ik99.alarm_presentation.flow.delegation.alarmringtonedelegate.AlarmPlayerDelegateImplementation
import com.poprigun4ik99.alarm_presentation.flow.delegation.alarmsetupdelegate.AlarmSetupDelegateImplementation
import com.poprigun4ik99.alarm_presentation.flow.delegation.notificationdelegate.NotificationDelegateImplementation
import com.poprigun4ik99.alarm_presentation.flow.workers.ClearOldAlarmsWorker
import com.poprigun4ik99.domain.delegates.alarmdelegate.AlarmSetupDelegate
import com.poprigun4ik99.domain.delegates.alarmringtonedelegate.AlarmRingtoneDelegate
import com.poprigun4ik99.domain.delegates.notificationdelegate.NotificationDelegate
import com.poprigun4ik99.domain.repositories.AlarmRepository
import com.poprigun4ik99.domain.usecases.CancelAlarmUseCase
import com.poprigun4ik99.domain.usecases.MarkAlarmAsPassedUseCase
import com.poprigun4ik99.domain.usecases.RemoveOldAlarmUseCase
import com.poprigun4ik99.domain.usecases.SetupAlarmUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module


val appModule = module {
    factory { RemoveOldAlarmUseCase(get()) }
    factory { CancelAlarmUseCase(get(), get()) }
    factory { SetupAlarmUseCase(get(), get()) }
    factory { MarkAlarmAsPassedUseCase(get()) }


    factory<AlarmSetupDelegate> { AlarmSetupDelegateImplementation(androidContext())}
    //factory<AlarmRingtoneDelegate> { AlarmRingtoneDelegateImplementation(androidContext())}
    factory<AlarmRingtoneDelegate> { AlarmPlayerDelegateImplementation(androidContext())}
    factory<NotificationDelegate> { NotificationDelegateImplementation(androidContext())}

    factory<AlarmRepository> { AlarmRepositoryImplementation(AppDataBase.getInstance(androidContext()).alarmDao) }

    worker { ClearOldAlarmsWorker(androidContext(), get(), get()) }
}