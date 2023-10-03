package com.example.health_and_fitness.Step_Counter.Repository

import com.example.health_and_fitness.Step_Counter.Model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
}