package com.example.health_and_fitness.Step_Counter.Model

import java.time.LocalDate

data class StepCounterState(
    val date: LocalDate,
    val steps: Int,
    val goal: Int,
    val distanceTravelled: Double,
    val calorieBurned: Int
)
