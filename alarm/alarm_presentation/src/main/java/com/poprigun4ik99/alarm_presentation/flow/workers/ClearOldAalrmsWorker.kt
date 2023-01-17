package com.poprigun4ik99.alarm_presentation.flow.workers


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.poprigun4ik99.domain.usecases.RemoveOldAlarmUseCase
import org.koin.core.component.KoinComponent

class ClearOldAlarmsWorker(ctx: Context, private val removeOldAlarmUseCase: RemoveOldAlarmUseCase, params: WorkerParameters): CoroutineWorker(ctx, params),
    KoinComponent {

    override suspend fun doWork(): Result {
        removeOldAlarmUseCase.execute()
        return Result.success()

    }

    companion object {
        const val OLD_ALARMS_GARBAGE_COLLECTOR_TAG = "OLD_ALARMS_GARBAGE_COLLECTOR_TAG"
    }
}