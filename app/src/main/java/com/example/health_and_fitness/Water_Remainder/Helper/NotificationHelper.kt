package com.example.health_and_fitness.Water_Remainder.Helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.CaseMap.Title
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.health_and_fitness.Activitys.Water_Remainder
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Water_Remainder.AppUnits
import java.util.Calendar
import java.util.Date

class NotificationHelper(private val context: Context) {
    private var notificationManager: NotificationManager? = null

    private val CHANNEL_ONE_ID = "water_remainder_channel"
    private val CHANNEL_ONE_NAME = "Channel One"

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prefs =
                context.getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
            val notificationsNewMessageRingtone = prefs.getString(
                AppUnits.NOTIFICATION_TONE_URI_KEY,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
            )

            val notificationChannel = NotificationChannel(
                CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.setShowBadge(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            if (!notificationsNewMessageRingtone.isNullOrEmpty()) {
                val audioAttributes =
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                notificationChannel.setSound(
                    Uri.parse(notificationsNewMessageRingtone), audioAttributes
                )
            }
            getManager()!!.createNotificationChannel(notificationChannel)
        }
    }

    fun getNotification(
        title: String,
        body: String,
        notificationsTone: String?
    ): NotificationCompat.Builder {
        createChannels()
        val notification = NotificationCompat.Builder(context.applicationContext, CHANNEL_ONE_ID)
            .setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.ic_water)
            .setAutoCancel(true)
        notification.setShowWhen(true)
        notificationsTone?.let { notification.setSound(Uri.parse(it)) }
        val notificationIntent = Intent(context, Water_Remainder::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent = PendingIntent.getActivity(
            context,
            99,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notification.setContentIntent(contentIntent)
        return notification
    }

    private fun shallNotify(): Boolean {
        val prefs = context.getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
        val sqliteHelper = SqliteHelper(context)
        val precent = sqliteHelper.getIntook(AppUnits.getCurrentDate()!!) * 100 / prefs.getInt(
            AppUnits.TOTAL_INTAKE,
            0
        )
        var doNotDisturbOff = true
        val startTimetamp = prefs.getLong(AppUnits.WAKEUP_TIME, 0)
        val stopTimetamp = prefs.getLong(AppUnits.SLEEPING_TIME_KEY, 0)

        if (startTimetamp > 0 && stopTimetamp > 0) {
            val now = Calendar.getInstance().time
            val start = Date(startTimetamp)
            val stop = Date(stopTimetamp)

            doNotDisturbOff = compareTimes(now, start) >= 0 && compareTimes(now, stop) <= 0
        }
        return doNotDisturbOff && (precent < 100)
    }

    private fun compareTimes(currentTime: Date, timeToRun: Date): Long {
        val currentCal = Calendar.getInstance()
        currentCal.time = currentTime

        val runCal = Calendar.getInstance()
        runCal.time = timeToRun
        runCal.set(Calendar.DAY_OF_MONTH, currentCal.get(Calendar.DAY_OF_MONTH))
        runCal.set(Calendar.MONTH, currentCal.get(Calendar.MONTH))
        runCal.set(Calendar.YEAR, currentCal.get(Calendar.YEAR))

        return currentCal.timeInMillis - runCal.timeInMillis
    }

    fun notify(id: Long, notification: NotificationCompat.Builder?) {
        if (shallNotify()){
            getManager()!!.notify(id.toInt(),notification!!.build())
        }else{
            Log.i("AquaDroid","dnd period")
        }
    }

    private fun getManager(): NotificationManager? {
        if (notificationManager == null) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }
}