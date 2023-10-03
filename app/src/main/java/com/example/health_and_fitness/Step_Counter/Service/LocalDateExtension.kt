package com.example.health_and_fitness.Step_Counter.Service

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.health_and_fitness.Step_Counter.Model.Day
import com.google.android.material.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

operator fun ClosedRange<LocalDate>.iterator() = object : Iterator<LocalDate> {

    @RequiresApi(Build.VERSION_CODES.O)
    private var current = start.minusDays(1)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun hasNext(): Boolean {
        return current.isBefore(endInclusive)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun next(): LocalDate {
        if (current.isBefore(endInclusive)) {
            current = current.plusDays(1)
        }
        return current
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<Day>.toChartValues(
    max: Int,
    locale: Locale,
    activeDay: LocalDate
): List<ChartAdapter.ChartValue<LocalDate>> = map {
    val value = it.steps / max.toDouble()
    val weekdayName = it.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
    val isSelected = it.date.isEqual(activeDay)
    val barColor =
        if (isSelected) R.attr.colorPrimary
        else R.attr.colorPrimaryContainer
    val textColor =
        if (isSelected) R.attr.colorPrimary
        else R.attr.colorPrimaryContainer
    ChartAdapter.ChartValue(
        it.date,
        value = value,
        label = weekdayName,
        barColor = barColor,
        textColor = textColor
    )
}