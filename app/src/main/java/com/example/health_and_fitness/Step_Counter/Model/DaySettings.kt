package com.example.health_and_fitness.Step_Counter.Model

import java.time.LocalDate

data class DaySettings(
    val date: LocalDate,
    val goal: Int,
    val height: Int,
    val weight: Int,
    val stepLength: Int,
    val pace: Double
)
