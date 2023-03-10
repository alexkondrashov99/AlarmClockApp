package com.poprigun4ik99.alarm_presentation.flow.dashboard

import android.app.AlarmManager
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.work.*
import com.poprigun4ik99.alarm_presentation.flow.broadcastreceivers.TestDozeReceiver
import com.poprigun4ik99.alarm_presentation.flow.dashboard.alarmsetup.AlarmSetupActivity
import com.poprigun4ik99.alarm_presentation.flow.workers.ClearOldAlarmsWorker
import com.poprigun4ik99.alarm_presentation.ui.screen.view.AlarmDashboardScreen
import com.poprigun4ik99.alarm_presentation.ui.theme.FreeAlarmClockAppTheme
import java.time.Duration
import java.util.*


class AlarmDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launchAlarmGarbageCollector()

        setContent {
            FreeAlarmClockAppTheme {
                AlarmDashboardScreen(
                    //TODO make it through viewModel
                    onNavigateAddNewAlarm = { startActivity(Intent(this, AlarmSetupActivity::class.java)) },
                )
            }
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FreeAlarmClockAppTheme {
            AlarmDashboardScreen(
                //TODO make it through viewModel
                onNavigateAddNewAlarm = { startActivity(Intent(this, AlarmSetupActivity::class.java)) },
            )
        }
    }

//    private fun displayNextAlarmTime() {
//        findViewById<TextView>(R.id.tvUpcomingAlarm)?.apply {
//            text =
//                this@AlarmDashboardActivity.getSystemService<AlarmManager>()?.nextAlarmClock?.triggerTime?.run {
//                    toRegularDateString() + " " + toRegularTimeString()
//                } ?: "no alarms"
//        }
//    }

    private fun checkAndroid12Permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(this, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    this.startActivity(intent)
                }
            }
        }
    }

    private fun registerDozeReceiver() {
        val filter = IntentFilter()
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED)
        registerReceiver(TestDozeReceiver(), filter)
    }

    private fun checkPermissionsXiaomi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                if ("xiaomi" == Build.MANUFACTURER.lowercase(Locale.ROOT)) {
                    val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                    intent.setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity"
                    )
                    intent.putExtra("extra_pkgname", packageName)
                    AlertDialog.Builder(this)
                        .setTitle("Please Enable the additional permissions")
                        .setMessage("You will not receive notifications while the app is in background if you disable these permissions")
                        .setPositiveButton("Go to Settings",
                            DialogInterface.OnClickListener { dialog, which -> startActivity(intent) })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setCancelable(false)
                        .show()
                } else {
                    val overlaySettings = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivityForResult(overlaySettings, 1)
                }
            }
        }
    }

    private fun launchAlarmGarbageCollector() {

        WorkManager.getInstance(application).let { manager ->
            val constraints = Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<ClearOldAlarmsWorker>(
                flexTimeInterval = Duration.ofMillis(
                    PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
                ),
                repeatInterval = Duration.ofHours(1)
            )
                .setConstraints(constraints)
                .build()

            manager.enqueueUniquePeriodicWork(
                ClearOldAlarmsWorker.OLD_ALARMS_GARBAGE_COLLECTOR_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }

}