package com.poprigun4ik99.alarm_presentation.ui.screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardViewModel
import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.toRegularDateString
import com.poprigun4ik99.domain.toRegularTimeString


@Composable
fun AlarmItem(
    alarm: AlarmDashboardViewModel.AlarmUiItem,
    onAddNewAlarmClicked: () -> Unit,
    onAlarmClicked: (AlarmRecord) -> Unit,
    onAlarmLongClicked: (AlarmRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    when (alarm) {
        AlarmDashboardViewModel.AlarmUiItem.AddNewAlarm -> {
            AddAlarmItem(
                onAddNewAlarmClicked = onAddNewAlarmClicked,
                modifier
            )
        }
        is AlarmDashboardViewModel.AlarmUiItem.AlarmItem -> {
            AlarmUiItem(
                item = alarm,
                onAlarmClicked = onAlarmClicked,
                onAlarmLongClicked = onAlarmLongClicked,
                modifier
            )
        }
    }
}


@Composable
private fun AddAlarmItem(
    onAddNewAlarmClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onAddNewAlarmClicked()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "+ Add New Alarm",
            fontSize = 28.sp,
            color = Color(0xFF747474),
            modifier = modifier.padding(
                PaddingValues(
                    vertical = 16.dp
                )
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AlarmUiItem(
    item: AlarmDashboardViewModel.AlarmUiItem.AlarmItem,
    onAlarmClicked: (AlarmRecord) -> Unit,
    onAlarmLongClicked: (AlarmRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onAlarmClicked(item.alarmRecord)
                }, onLongClick = {
                    onAlarmLongClicked(item.alarmRecord)
                }
            ).padding(
                PaddingValues(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
            ),
    ) {
        Column(
            modifier = modifier
                .width(with(LocalDensity.current) {100.sp.toDp()}),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.alarmRecord.timeStamp.toRegularTimeString(),
                fontSize = 28.sp,

            )
            Text(
                text = item.alarmRecord.timeStamp.toRegularDateString(),
                fontSize = 14.sp
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = item.alarmRecord.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    Divider(color = Color.Black, thickness = 1.dp,
        modifier = modifier.padding(PaddingValues(horizontal = 10.dp)))
}