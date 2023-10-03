package com.example.health_and_fitness.Step_Counter.Settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.health_and_fitness.Step_Counter.Model.DaySettings
import com.example.health_and_fitness.Step_Counter.Repository.DayRepositoryImpl
import com.example.health_and_fitness.Step_Counter.StepCounterApplication
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate

class SettingsViewModel(private val settingsUseCases: SettingsUseCases) : ViewModel() {

    private var observeSettingsChangesJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeSettingsChanges() {
        observeSettingsChangesJob?.cancel()
        observeSettingsChangesJob = settingsUseCases.getSettings().onEach {
            settingsUseCases.updateDaySettings(
                DaySettings(
                    date = LocalDate.now(),
                    goal = it.dailyGoal,
                    height = it.height,
                    weight = it.weight,
                    stepLength = it.stepLength,
                    pace = it.pace
                )
            )
        }.launchIn(viewModelScope)
    }

    companion object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as StepCounterApplication

            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val dayDatabase = application.stepCounterDatabase
            val dayRepository = DayRepositoryImpl(dayDatabase.dayDao)

            val settingsUseCases = SettingsUseCases(settingsRepository, dayRepository)

            return SettingsViewModel(settingsUseCases) as T
        }
    }
}