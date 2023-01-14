package com.poprigun4ik99.freealarmclock

import android.app.Application
import com.poprigun4ik99.alarm_presentation.koin.appModule
import com.poprigun4ik99.alarm_presentation.koin.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class AlarmApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@AlarmApplication)
            modules(appModule, viewModels)
        }
    }
}