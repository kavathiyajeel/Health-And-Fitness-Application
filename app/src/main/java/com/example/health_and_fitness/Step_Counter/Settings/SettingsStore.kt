package com.example.health_and_fitness.Step_Counter.Settings

import com.example.health_and_fitness.Step_Counter.Model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsStore {
    fun getSettings(): Flow<Settings>
}