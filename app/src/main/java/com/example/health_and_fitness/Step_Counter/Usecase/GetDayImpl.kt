package com.example.health_and_fitness.Step_Counter.Usecase

import com.example.health_and_fitness.Step_Counter.Model.Day
import com.example.health_and_fitness.Step_Counter.Model.of
import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import com.example.health_and_fitness.Step_Counter.Repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

class GetDayImpl(
    private val dayRepository: DayRepository,
    private val settingsRepository: SettingsRepository
) : GetDay {
    override fun invoke(date: LocalDate): Flow<Day> {
        val settingsFlow = settingsRepository.getSettings()
        val dayFlow = dayRepository.getDay(date)

        return settingsFlow.combine(dayFlow) { settings, day ->
            day ?: Day.of(date, settings, steps = 0)
        }
    }
}