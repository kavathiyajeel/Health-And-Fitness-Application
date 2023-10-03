package com.example.health_and_fitness.Step_Counter.Service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.health_and_fitness.Step_Counter.Model.StatsDetailsState
import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import com.example.health_and_fitness.Step_Counter.Repository.DayRepositoryImpl
import com.example.health_and_fitness.Step_Counter.Settings.SettingsRepositoryImpl
import com.example.health_and_fitness.Step_Counter.Settings.SettingsStoreImpl
import com.example.health_and_fitness.Step_Counter.StepCounterApplication
import com.example.health_and_fitness.Step_Counter.Usecase.DayUseCases
import com.example.health_and_fitness.Step_Counter.Usecase.StatsDetailsUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
class StatsDetailsViewModel(
    private val dayUseCases: DayUseCases,
    statsDetailsUseCases: StatsDetailsUseCases,
    currentDateFlow: StateFlow<LocalDate>
) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _day = MutableStateFlow(
        StatsDetailsState(
            date = LocalDate.MIN,
            stepsTaken = 0,
            calorieBurned = 0,
            distanceTravelled = 0.0, chartDateRange = currentDateFlow.value..currentDateFlow.value
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    val day: StateFlow<StatsDetailsState> = _day.asStateFlow()

    init {
        selectDay(currentDateFlow.value)

        viewModelScope.launch {
            val firstDateFlow = statsDetailsUseCases.getFirstDate()
            firstDateFlow
                .combine(currentDateFlow) { firstDate, currentDate ->
                    firstDate..currentDate
                }.collect { dateRange ->
                    _day.value = day.value.copy(chartDateRange = dateRange)
                }
        }
    }

    private var selectDateJob: Job? = null

    fun selectDay(date: LocalDate) {
        selectDateJob?.cancel()
        selectDateJob = dayUseCases.getDay(date).onEach {
            _day.value = day.value.copy(
                date = it.date,
                stepsTaken = it.steps,
                calorieBurned = it.calorieBurned.roundToInt(),
                distanceTravelled = it.distanceTravelled
            )
        }.launchIn(viewModelScope)
    }

    companion object Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

            val application = checkNotNull(extras[APPLICATION_KEY]) as StepCounterApplication

            val dayDatebase = application.stepCounterDatabase
            val dayRepository = DayRepositoryImpl(dayDatebase.dayDao)
            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val dayUseCases = DayUseCases(dayRepository, settingsRepository)
            val statsDetailsUseCases = StatsDetailsUseCases(dayRepository)


            return StatsDetailsViewModel(
                dayUseCases,
                statsDetailsUseCases,
                application.currentDate
            ) as T

        }
    }

}