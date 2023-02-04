package com.poprigun4ik99.freealarmclock

import android.app.Application
import android.util.Log
import com.poprigun4ik99.alarm_di.koin.appModule
import com.poprigun4ik99.alarm_di.koin.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin

class AlarmApplication: Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        Log.v("ALESHA", "Application onCreate")
        startKoin{
            androidLogger()
            androidContext(this@AlarmApplication)
            workManagerFactory()
            modules(
                appModule,
                viewModels
            )
        }
    }
}