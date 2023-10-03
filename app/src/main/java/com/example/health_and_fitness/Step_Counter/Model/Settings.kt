package com.example.health_and_fitness.Step_Counter.Model

data class Settings(
    val dailyGoal: Int,
    val stepLength: Int,
    val height: Int,
    val weight: Int,
    val pace: Double
)