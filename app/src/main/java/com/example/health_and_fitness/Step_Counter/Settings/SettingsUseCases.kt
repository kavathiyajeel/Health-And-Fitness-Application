package com.example.health_and_fitness.Step_Counter.Settings

import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import com.example.health_and_fitness.Step_Counter.Repository.SettingsRepository

class SettingsUseCases(
    settingsRepository: SettingsRepository,
    dayRepository: DayRepository,
) {

    val getSettings: GetSettings = GetSettingsImpl(settingsRepository)
    val updateDaySettings: UpdateDaySettings = UpdateDaySettingsImpl(dayRepository)
}