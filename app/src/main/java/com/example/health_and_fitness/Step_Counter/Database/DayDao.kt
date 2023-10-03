package com.example.health_and_fitness.Step_Counter.Database


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.health_and_fitness.Step_Counter.Model.Day
import com.example.health_and_fitness.Step_Counter.Model.DaySettings
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
@Dao
interface DayDao {

    @Query("SELECT * FROM day ORDER BY date ASC LIMIT 1")
    fun getFirstDay(): Flow<Day?>

    @Query("SELECT * FROM day WHERE  date=:date")
    fun getDay(date: LocalDate): Flow<Day?>

    @Query("SELECT * FROm day")
    suspend fun getAllDays(): List<Day>

    @Query("SELECT * FROM day WHERE date BETWEEN:start AND:endInclusive")
    fun getDays(start: LocalDate, endInclusive: LocalDate): Flow<List<Day>>

    @Upsert
    suspend fun upsertDay(day: Day)

    @Update(entity = Day::class)
    suspend fun updateDaySettings(day: DaySettings)
}