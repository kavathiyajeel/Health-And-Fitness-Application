package com.example.health_and_fitness.Medicine_Remainder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.example.health_and_fitness.Medicine_Remainder.Activitys.Edit_Medicine
import com.example.health_and_fitness.Medicine_Remainder.Database.RemainderDatabase
import com.example.health_and_fitness.R
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var mAlarmManager: AlarmManager
    private lateinit var mPendingIntent: PendingIntent

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "medicine_remainder_channel"
        private const val NOTIFICATION_ID = 0x1
        private const val PENDING_INTENT_ID = 0x1
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Medicine Remainder Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            // Configure channel settings (optional)
            channel.description = "Medicine Remainder Notifications"
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(0, 250, 250, 250) // Custom vibration pattern

            notificationManager.createNotificationChannel(channel)
        }

            val mReceivedID = intent.getStringExtra(Edit_Medicine.EXTRA_REMINDER_ID)?.toInt()

            // Get notification title from Reminder Database
            val rb = RemainderDatabase(context)
            val reminder = rb.getRemainder(mReceivedID ?: 0)
            val mTitle = reminder.title

            // Create intent to open ReminderEditActivity on notification click
            val editIntent = Intent(context, Edit_Medicine::class.java)
            editIntent.putExtra(Edit_Medicine.EXTRA_REMINDER_ID, mReceivedID.toString())
            val mClick = PendingIntent.getActivity(
                context,
                mReceivedID ?: 0,
                editIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Create Notification
            val mBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_icon_round)
                .setContentTitle("It's time to take your Medicine..")
                .setTicker(mTitle)
                .setContentText(mTitle)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(mClick)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)

            val nManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.notify(mReceivedID ?: 0, mBuilder.build())

        }

        fun setAlarm(context: Context, calendar: Calendar, ID: Int) {
            mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Put Reminder ID in Intent Extra
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(Edit_Medicine.EXTRA_REMINDER_ID, ID.toString())
            mPendingIntent = PendingIntent.getBroadcast(
                context,
                ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Calculate notification time
            val c = Calendar.getInstance()
            val currentTime = c.timeInMillis
            val diffTime = calendar.timeInMillis - currentTime

            // Start alarm using notification time
            mAlarmManager.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                mPendingIntent
            )

            // Restart alarm if device is rebooted
            val receiver = ComponentName(context, BootReceiver::class.java)
            val pm = context.packageManager
            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        fun setRepeatAlarm(context: Context, calendar: Calendar, ID: Int, RepeatTime: Long) {
            mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Put Reminder ID in Intent Extra
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(Edit_Medicine.EXTRA_REMINDER_ID, ID.toString())
            mPendingIntent = PendingIntent.getBroadcast(
                context,
                ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Calculate notification time
            val c = Calendar.getInstance()
            val currentTime = c.timeInMillis
            val diffTime = calendar.timeInMillis - currentTime

            // Start alarm using initial notification time and repeat interval time
            mAlarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                RepeatTime, mPendingIntent
            )

            // Restart alarm if device is rebooted
            val receiver = ComponentName(context, BootReceiver::class.java)
            val pm = context.packageManager
            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        fun cancelAlarm(context: Context, ID: Int) {
            mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Cancel Alarm using Reminder ID
            mPendingIntent = PendingIntent.getBroadcast(
                context,
                ID,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            mAlarmManager.cancel(mPendingIntent)

            // Disable alarm
            val receiver = ComponentName(context, BootReceiver::class.java)
            val pm = context.packageManager
            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }
