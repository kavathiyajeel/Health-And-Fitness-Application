package com.example.health_and_fitness.Step_Counter.Model

import java.time.LocalDate

data class StepCounterEvent(
    val stepCount: Int,
    val eventDate: LocalDate,
)
