package com.example.health_and_fitness.Step_Counter.Settings

import com.example.health_and_fitness.Step_Counter.Model.Settings
import com.example.health_and_fitness.Step_Counter.Repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(private val settingsStore: SettingsStore) : SettingsRepository {
    override fun getSettings(): Flow<Settings> {
        return settingsStore.getSettings()
    }
}