package com.example.health_and_fitness.Water_Remainder.Recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.health_and_fitness.Water_Remainder.AppUnits
import com.example.health_and_fitness.Water_Remainder.Helper.AlarmHelper

class BootReceiver : BroadcastReceiver() {
    private val alarm = AlarmHelper()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action != null) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                val prefs = context!!.getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
                val notificationFrequency = prefs.getInt(AppUnits.NOTIFICATION_FREQUENCY_KEY, 60)
                val notificationsNewMessage = prefs.getBoolean("notifications_new_message", true)
                alarm.cancelAlarm(context)
                if (notificationsNewMessage) {
                    alarm.setAlarm(context, notificationFrequency.toLong())
                }
            }
        }
    }
}