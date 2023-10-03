package com.example.health_and_fitness.Step_Counter.Usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.health_and_fitness.Step_Counter.Model.Day
import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetWeek {

    operator fun invoke(startingAt: LocalDate): Flow<List<Day>>
}

class GetWeekImpl(
    private val dayRepository: DayRepository
) : GetWeek {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun invoke(startingAt: LocalDate): Flow<List<Day>> {
        val endingAt = startingAt.plusDays(6)
        return dayRepository.getDays(startingAt..endingAt)
    }
}