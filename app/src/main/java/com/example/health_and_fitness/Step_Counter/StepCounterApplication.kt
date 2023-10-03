package com.example.health_and_fitness.Step_Counter

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Step_Counter.Database.StepCounterDatabase
import com.example.health_and_fitness.Step_Counter.Settings.SettingsStore
import com.example.health_and_fitness.Step_Counter.Settings.SettingsStoreImpl
import com.google.android.material.color.DynamicColors
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class StepCounterApplication : Application() {
    lateinit var settingsStore: SettingsStore
    lateinit var stepCounterDatabase: StepCounterDatabase


    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = MutableStateFlow<LocalDate>(LocalDate.now())

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false)
        registerMidnightTimer()


        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        settingsStore = SettingsStoreImpl(sharedPreferences)

        stepCounterDatabase = Room.databaseBuilder(
            applicationContext, StepCounterDatabase::class.java, StepCounterDatabase.DATABASE_NAME
        ).build()
    }

    private fun registerMidnightTimer() {
        val intentFilter=IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }

        registerReceiver(midnightBroadcastReceiver, intentFilter)

    }
    private val midnightBroadcastReceiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            val today = LocalDate.now()
            if (today != currentDate.value) {
                currentDate.value = today
            }
        }
    }
}