package com.example.health_and_fitness.Step_Counter.Settings

import com.example.health_and_fitness.Step_Counter.Model.Settings
import com.example.health_and_fitness.Step_Counter.Repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

interface GetSettings {
    operator fun invoke(): Flow<Settings>
}
class GetSettingsImpl(
    private val repository: SettingsRepository
) : GetSettings {

    override fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}