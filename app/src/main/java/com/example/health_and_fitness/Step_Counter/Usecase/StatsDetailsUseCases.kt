package com.example.health_and_fitness.Step_Counter.Usecase

import com.example.health_and_fitness.Step_Counter.Repository.DayRepository

class StatsDetailsUseCases(dayRepository: DayRepository) {
val getFirstDate:GetFirstDate=GetFirstDateImpl(dayRepository)
}