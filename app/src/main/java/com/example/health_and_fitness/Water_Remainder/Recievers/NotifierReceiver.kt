package com.example.health_and_fitness.Water_Remainder.Recievers

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Water_Remainder.AppUnits
import com.example.health_and_fitness.Water_Remainder.Helper.NotificationHelper

class NotifierReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val prefs = context.getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
        val notificationsTone = prefs.getString(
            AppUnits.NOTIFICATION_TONE_URI_KEY,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
        )

        val title = context.resources.getString(R.string.app_name)
        val messageToShow = prefs.getString(
            AppUnits.NOTIFICATION_MSG_KEY,
            context.resources.getString(R.string.pref_notification_message_value)
        )

        val nHelper = NotificationHelper(context)
        @SuppressLint("ResourceType") val nBuilder = messageToShow?.let {
            nHelper.getNotification(title, it, notificationsTone)
        }

        nHelper.notify(1, nBuilder)
    }
}