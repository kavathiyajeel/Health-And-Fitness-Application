package com.example.health_and_fitness.Step_Counter.Usecase

import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class IncrementStepCountImpl(
    private val repository: DayRepository,
    private val getDayUseCase: GetDay
) : IncrementStepCount {
    override suspend fun invoke(date: LocalDate, by: Int) {
        val day = getDayUseCase(date).first()
        val updateDay = day.copy(steps = day.steps + by)
        repository.upsertDay(updateDay)
    }
}