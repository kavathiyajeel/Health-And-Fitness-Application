package com.example.health_and_fitness.Step_Counter.Service

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.health_and_fitness.Step_Counter.Model.Day
import com.example.health_and_fitness.Step_Counter.Repository.DayRepositoryImpl
import com.example.health_and_fitness.Step_Counter.StepCounterApplication
import com.example.health_and_fitness.Step_Counter.Usecase.StatsChartPageUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatsChartPageViewModel(private val statsChartPageUseCases: StatsChartPageUseCases) :
    ViewModel() {

    private val _week = MutableStateFlow<List<Day>>(emptyList())
    val week: StateFlow<List<Day>> = _week.asStateFlow()
    private var getWeekJob: Job? = null
    @RequiresApi(Build.VERSION_CODES.O)
    fun selectWeek(firstDate: LocalDate) {
        getWeekJob?.cancel()
        getWeekJob = viewModelScope.launch {
            statsChartPageUseCases.getWeek(firstDate).collect { week ->
                _week.value = week.alignWeek(firstDate)
            }
        }
    }
    companion object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as StepCounterApplication

            val forestDatabase = application.stepCounterDatabase
            val dayRepository = DayRepositoryImpl(forestDatabase.dayDao)
            val statsChartPageUseCases = StatsChartPageUseCases(dayRepository)

            return StatsChartPageViewModel(statsChartPageUseCases) as T
        }
    }
}