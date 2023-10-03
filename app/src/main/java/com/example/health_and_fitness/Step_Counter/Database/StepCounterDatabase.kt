package com.example.health_and_fitness.Step_Counter.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.health_and_fitness.Step_Counter.Model.Day


@Database(entities = [Day::class], version = 1)
@TypeConverters(Converters::class)
abstract class StepCounterDatabase : RoomDatabase() {
    abstract val dayDao: DayDao

    companion object {
        const val DATABASE_NAME = "step_counter"
    }
}