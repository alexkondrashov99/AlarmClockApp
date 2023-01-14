package com.poprigun4ik99.alarm_presentation.flow.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.poprigun4ik99.alarm_presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.*
import java.util.*


class AlarmDashboardActivity: AppCompatActivity() {

    private val viewModel: AlarmDashboardViewModel by viewModel()

    private val rvAlarms: RecyclerView by lazy { findViewById(R.id.rvAlarms) }
    private val alarmAdapter: AlarmAdapter by lazy { AlarmAdapter(
        onItemClick = {

        },
        onItemLongClick = {
            viewModel.cancelAlarm(it.id)
        },
        onAddClick = {
            onAddAlarmClick()
        }
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //checkPermissions()
        setContentView(R.layout.activity_setup_alarm)
        rvAlarms.adapter = alarmAdapter
        with(viewModel) {
            observeAlarmRecords()
            alarmRecordsLiveData.observe(this@AlarmDashboardActivity) {
                alarmAdapter.submitList(it)
//                val str = it.joinToString(separator = "\n") { alarmRecord ->
//                    "$alarmRecord " + Instant.ofEpochMilli(alarmRecord.timeStamp)
//                        .atZone(ZoneId.systemDefault())
//                }
//                tvAlarmInfo.text = str
            }
        }

    }

    private fun onAddAlarmClick() {
        val materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(LocalDateTime.now().hour)
            .setMinute(0)
            .setTitleText("Pick time for alarm")
            .build()

        materialTimePicker.addOnPositiveButtonClickListener {
            val time = ZonedDateTime.of(
                ZonedDateTime.now().year,
                ZonedDateTime.now().monthValue,
                ZonedDateTime.now().dayOfMonth,
                materialTimePicker.hour,
                materialTimePicker.minute,
                0,
                0,
                ZoneId.systemDefault()
            )
            viewModel.setupAlarm(time.toInstant().toEpochMilli())
            //viewModel.setupAlarm(System.currentTimeMillis() + 1000 * 5)
        }
        materialTimePicker.show(supportFragmentManager, "tag_picker");
    }

    private fun checkPermissions() {
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
}