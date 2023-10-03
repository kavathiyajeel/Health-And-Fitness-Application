package com.example.health_and_fitness.Step_Counter.Usecase

import com.example.health_and_fitness.Step_Counter.Repository.DayRepository

class StatsChartPageUseCases(dayRepository: DayRepository) {
    val getWeek: GetWeek = GetWeekImpl(dayRepository)
}