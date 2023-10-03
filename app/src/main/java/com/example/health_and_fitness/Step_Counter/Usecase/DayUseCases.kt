package com.example.health_and_fitness.Step_Counter.Usecase

import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import com.example.health_and_fitness.Step_Counter.Repository.SettingsRepository

class DayUseCases(dayRepository: DayRepository, settingsRepository: SettingsRepository) {

    val getDay: GetDay = GetDayImpl(dayRepository, settingsRepository)
    val incrementStepCount: IncrementStepCount = IncrementStepCountImpl(dayRepository, getDay)
}