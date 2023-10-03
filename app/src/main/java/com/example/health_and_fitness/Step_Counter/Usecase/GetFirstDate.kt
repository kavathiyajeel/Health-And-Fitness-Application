package com.example.health_and_fitness.Step_Counter.Usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.health_and_fitness.Step_Counter.Repository.DayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

interface GetFirstDate {

    operator fun invoke(): Flow<LocalDate>
}
class GetFirstDateImpl(private val dayRepository: DayRepository):GetFirstDate{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun invoke(): Flow<LocalDate> {
        return dayRepository.getFirstDay().map { it?.date?: LocalDate.now() }
    }

}
