package com.poprigun4ik99.alarm_presentation.ui.screen.view

import android.app.AlarmManager
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardViewModel
import com.poprigun4ik99.alarm_presentation.ui.screen.components.AlarmItem
import com.poprigun4ik99.domain.toRegularDateString
import com.poprigun4ik99.domain.toRegularTimeString
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AlarmDashboardScreen(
    viewModel: AlarmDashboardViewModel = koinViewModel(),
    onNavigateAddNewAlarm: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Handle navigation
     */
    EventEffect(
        event = uiState.navigation,
        onConsumed = viewModel::consumeNavigation,
        action = {
            when(it) {
                AlarmDashboardViewModel.Navigation.AddNewAlarm -> onNavigateAddNewAlarm()
                is AlarmDashboardViewModel.Navigation.EditAlarm -> {
                    /* Unit */
                }
            }
        }
    )

    /**
     * UI
     */
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            },
        bottomBar = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = LocalContext.current.getSystemService(AlarmManager::class.java)?.nextAlarmClock?.triggerTime?.run {
                    toRegularDateString() + " " + toRegularTimeString()
                } ?: "no upcoming alarms",
                textAlign = TextAlign.Center
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
        ) {
            items(
                items = uiState.alarmsList,
                key = { alarm ->
                    (alarm as? AlarmDashboardViewModel.AlarmUiItem.AlarmItem)?.alarmRecord?.id
                        ?: -1
                }
            ) {alarm ->
                AlarmItem(
                    alarm,
                    onAlarmClicked = { },
                    onAlarmLongClicked = { viewModel.cancelAlarm(it.id) },
                    onAddNewAlarmClicked = viewModel::navigateAddNewAlarm,
                    modifier = Modifier.animateItemPlacement(animationSpec = tween(500))
                )
            }
        }
    }
}