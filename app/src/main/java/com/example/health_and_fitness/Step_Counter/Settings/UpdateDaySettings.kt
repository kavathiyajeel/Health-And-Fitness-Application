package com.example.health_and_fitness.Step_Counter.Settings

import com.example.health_and_fitness.Step_Counter.Model.DaySettings
import com.example.health_and_fitness.Step_Counter.Repository.DayRepository

interface UpdateDaySettings {

    suspend operator fun invoke(daySettings: DaySettings)
}

class UpdateDaySettingsImpl(
    private val dayRepository: DayRepository
) : UpdateDaySettings {

    override suspend fun invoke(daySettings: DaySettings) {
        dayRepository.updateDaySettings(daySettings)
    }
}