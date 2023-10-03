package com.example.health_and_fitness.Medicine_Remainder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.health_and_fitness.Medicine_Remainder.Database.RemainderDatabase
import java.util.Calendar

class BootReceiver: BroadcastReceiver() {

    private var mReceivedID: Int = 0
    private var mRepeat: String? = null
    private var mRepeatNo: String? = null
    private var mRepeatType: String? = null
    private var mActive: String? = null
    private var mDate: String? = null
    private var mTime: String? = null
    private lateinit var mDateSplit: Array<String>
    private lateinit var mTimeSplit: Array<String>
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mDay: Int = 0
    private var mRepeatTime: Long = 0

    private lateinit var mCalendar: Calendar
    private lateinit var mAlarmReceiver: AlarmReceiver

    // Constant values in milliseconds
    private companion object {
        const val milMinute = 60000L
        const val milHour = 3600000L
        const val milDay = 86400000L
        const val milWeek = 604800000L
        const val milMonth = 2592000000L
    }
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            val rb = RemainderDatabase(context)
            mCalendar = Calendar.getInstance()
            mAlarmReceiver = AlarmReceiver()

            val reminders = rb.getAllRemainders()

            for (rm in reminders) {
                mReceivedID = rm.id
                mRepeat = rm.repeat
                mRepeatNo = rm.repeatNo
                mRepeatType = rm.repeatType
                mActive = rm.active
                mDate = rm.date
                mTime = rm.time

                mDateSplit = mDate!!.split("/").toTypedArray()
                mTimeSplit = mTime!!.split(":").toTypedArray()

                mDay = mDateSplit[0].toInt()
                mMonth = mDateSplit[1].toInt()
                mYear = mDateSplit[2].toInt()
                mHour = mTimeSplit[0].toInt()
                mMinute = mTimeSplit[1].toInt()

                mCalendar.set(Calendar.MONTH, --mMonth)
                mCalendar.set(Calendar.YEAR, mYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay)
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour)
                mCalendar.set(Calendar.MINUTE, mMinute)
                mCalendar.set(Calendar.SECOND, 0)

                // Cancel existing notification of the reminder by using its ID
                // mAlarmReceiver.cancelAlarm(context, mReceivedID);

                // Check repeat type
                mRepeatTime = when (mRepeatType) {
                    "Minute" -> Integer.parseInt(mRepeatNo) * milMinute
                    "Hour" -> Integer.parseInt(mRepeatNo) * milHour
                    "Day" -> Integer.parseInt(mRepeatNo) * milDay
                    "Week" -> Integer.parseInt(mRepeatNo) * milWeek
                    "Month" -> Integer.parseInt(mRepeatNo) * milMonth
                    else -> 0L
                }

                // Create a new notification
                if (mActive == "true") {
                    if (mRepeat == "true") {
                        mAlarmReceiver.setRepeatAlarm(context, mCalendar, mReceivedID, mRepeatTime)
                    } else if (mRepeat == "false") {
                        mAlarmReceiver.setAlarm(context, mCalendar, mReceivedID)
                    }
                }
            }
        }
    }
}