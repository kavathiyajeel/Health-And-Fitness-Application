package com.example.health_and_fitness.Step_Counter.Model

import java.time.LocalDate

data class ProgressState(
    val date: LocalDate,
    val stepsTaken: Int,
    val dailyGoal: Int,
    val calorieBurned: Int,
    val distanceTravelled: Double,
)
