package com.example.health_and_fitness.Step_Counter.Service

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.health_and_fitness.Step_Counter.Model.Day
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun List<Day>.alignWeek(
    firstDay: LocalDate,
    lastDay: LocalDate = firstDay.plusDays(6),
): List<Day> {
    val alignedWeek = mutableListOf<Day>()
    for (date in firstDay..lastDay) {
        val currentDay = singleOrNull { it.date == date }
        alignedWeek.add(currentDay ?: Day(date, goal = 0))
    }
    return alignedWeek
}
