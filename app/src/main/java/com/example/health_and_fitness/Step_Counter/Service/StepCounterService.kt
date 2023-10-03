package com.example.health_and_fitness.Step_Counter.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.health_and_fitness.Activitys.Step_Counter
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Step_Counter.Model.StepCounterState
import com.example.health_and_fitness.Step_Counter.Repository.DayRepositoryImpl
import com.example.health_and_fitness.Step_Counter.Settings.SettingsRepositoryImpl
import com.example.health_and_fitness.Step_Counter.StepCounterApplication
import com.example.health_and_fitness.Step_Counter.Usecase.DayUseCases
import kotlinx.coroutines.launch
import java.time.LocalDate


class StepCounterService : LifecycleService(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var controller: StepCounterController

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 0x1
        private const val PENDING_INTENT_ID = 0x1
    }

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notiticationchannel = createNotificationChannel()
            registerNotificationChannel(notiticationchannel)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        registerStepCounter(sensorManager)

        val application = application as StepCounterApplication

        val settingsStore = application.settingsStore
        val settingsRepository = SettingsRepositoryImpl(settingsStore)
        val dayDatabase = application.stepCounterDatabase
        val dayRepository = DayRepositoryImpl(dayDatabase.dayDao)
        val dayUseCases = DayUseCases(dayRepository, settingsRepository)


        controller = StepCounterController(dayUseCases, lifecycleScope, application.currentDate)

        //create notification
        val notification = createNotification(controller.stats.value)
        startForeground(NOTIFICATION_ID, notification)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                controller.stats.collect {
                    val updatedNotification = createNotification(it)
                    notificationManager.notify(NOTIFICATION_ID, updatedNotification)
                }
            }
        }
    }

    private fun createNotification(state: StepCounterState): Notification = state.run {
        val title = resources.getQuantityString(R.plurals.step_count, steps, steps)
        val progress = if (goal == 0) 0 else steps * 100 / goal
        val content = getString(
            R.string.step_counter_stats, calorieBurned, distanceTravelled, progress
        )

        NotificationCompat.Builder(this@StepCounterService, NOTIFICATION_CHANNEL_ID)
            .setContentIntent(launchApplicationPendingIntent)
            .setSmallIcon(R.drawable.ic_step)
            .setContentTitle(title)
            .setContentText(content)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSilent(true)
            .build()
    }

    private val launchApplicationPendingIntent
        get(): PendingIntent {
            val intent = Intent(applicationContext, Step_Counter::class.java)
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            return PendingIntent.getActivity(this, PENDING_INTENT_ID, intent, flags)
        }

    private fun registerStepCounter(sensorManager: SensorManager) {
        val stepCounterSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    @RequiresApi(VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val eventStepCount = it.values[0].toInt()
            controller.onStepCountChanged(eventStepCount, LocalDate.now())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    @RequiresApi(VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val name = getString(R.string.step_counter_channel)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        return NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            setShowBadge(false)
        }
    }

    @RequiresApi(VERSION_CODES.O)
    private fun registerNotificationChannel(channel: NotificationChannel) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}