package com.example.health_and_fitness.Step_Counter.Repository

import com.example.health_and_fitness.Step_Counter.Model.Day
import com.example.health_and_fitness.Step_Counter.Model.DaySettings
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DayRepository {
    fun getFirstDay(): Flow<Day?>

    fun getDay(date: LocalDate): Flow<Day?>

    suspend fun getAllDays(): List<Day>

    fun getDays(range: ClosedRange<LocalDate>): Flow<List<Day>>

    suspend fun upsertDay(day: Day)

    suspend fun updateDaySettings(daySettings: DaySettings)
}