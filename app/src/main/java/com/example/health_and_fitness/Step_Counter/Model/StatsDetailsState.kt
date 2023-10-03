package com.example.health_and_fitness.Step_Counter.Model

import java.time.LocalDate

data class StatsDetailsState(
    val date: LocalDate,
    val stepsTaken: Int,
    val calorieBurned: Int,
    val distanceTravelled: Double,
    val chartDateRange: ClosedRange<LocalDate>
)
