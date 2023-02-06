package com.poprigun4ik99.alarm_presentation.flow.dashboard.alarmsetup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.datepicker.*
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.poprigun4ik99.alarm_presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

class AlarmSetupActivity : AppCompatActivity() {

    private val viewModel: AlarmSetupViewModel by viewModel()

    private val tvTime: TextView by lazy { findViewById(R.id.tvAlarmTime)}
    private val tvDate: TextView by lazy { findViewById(R.id.tvAlarmDate)}
    private val tvDescription: EditText by lazy { findViewById(R.id.etDescription)}
    private val btSubmit: Button by lazy { findViewById(R.id.btSubmit)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setup)

        observeViewModel()

        tvTime.setOnClickListener {
            openTimePickerDialog()
        }
        tvDate.setOnClickListener {
            openDatePickerDialog()
        }
        btSubmit.setOnClickListener {
            viewModel.onCreateAlarmClicked()
        }

        setupDescriptionField()

    }

    private fun setupDescriptionField() {
        tvDescription.setText(viewModel.getDescription())
        tvDescription.doOnTextChanged { text, start, before, count ->
            viewModel.setDescription(text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            dateLiveData.observe(this@AlarmSetupActivity) {
                tvDate.text = it
            }
            timeLiveData.observe(this@AlarmSetupActivity) {
                tvTime.text = getString(R.string.hours_minutes, it.hours, it.minutes)
            }
            navigateFinish.observe(this@AlarmSetupActivity) {
                finish()
            }
            showInvalidTimeError.observe(this@AlarmSetupActivity) {
                showInvalidTimeError()
            }
        }
    }

    private fun openTimePickerDialog() {
        val materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(LocalDateTime.now().hour)
            .setMinute(0)
            .setTitleText("Pick time for alarm")
            .build()

        materialTimePicker.addOnPositiveButtonClickListener {
            viewModel.setTime(materialTimePicker.hour,  materialTimePicker.minute )
        }
        materialTimePicker.show(supportFragmentManager, "time_picker");
    }


    private fun showInvalidTimeError() {
        Toast.makeText(this, "Invalid alarm time", Toast.LENGTH_SHORT).show()
    }

    private fun openDatePickerDialog() {
        val startTime = ZonedDateTime.now().toInstant().toEpochMilli()
        val endTime = ZonedDateTime.now().plusDays(AVAILABLE_ALARM_DAYS).toInstant().toEpochMilli()

        val dateValidatorMin: CalendarConstraints.DateValidator = DateValidatorPointForward.now()
        val dateValidatorMax: CalendarConstraints.DateValidator = DateValidatorPointBackward.before(endTime)

        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
        listValidators.add(dateValidatorMin)
        listValidators.add(dateValidatorMax)
        val validators = CompositeDateValidator.allOf(listValidators)

        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select alarm date")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(validators)
                    .build()
            )
            .build()

        materialDatePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(it)
        }

        materialDatePicker.show(supportFragmentManager, "date_picker");
    }

    companion object {
        const val AVAILABLE_ALARM_DAYS = 30L
    }
}