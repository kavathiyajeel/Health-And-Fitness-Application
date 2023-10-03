package com.example.health_and_fitness.Step_Counter.Usecase

import com.example.health_and_fitness.Step_Counter.Model.Day
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetDay {
    operator fun invoke(date: LocalDate): Flow<Day>
}