package com.example.health_and_fitness.Step_Counter.Usecase

import java.time.LocalDate

interface IncrementStepCount {
    suspend operator fun invoke(date: LocalDate, by: Int)
}