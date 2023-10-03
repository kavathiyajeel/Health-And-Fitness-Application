package com.example.health_and_fitness.Water_Remainder.Fragment

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import com.example.health_and_fitness.Activitys.Water_Remainder
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Water_Remainder.AppUnits
import com.example.health_and_fitness.Water_Remainder.AppUnits.Companion.NOTIFICATION_TONE_URI_KEY
import com.example.health_and_fitness.Water_Remainder.Helper.AlarmHelper
import com.example.health_and_fitness.Water_Remainder.Helper.SqliteHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Calendar

class Bottomsheet_dialog_water_remainder(private val context: Context) :
    BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var weight: String = ""
    private var workTime: String = ""
    private var customTarget: String = ""
    private var wakeupTime: Long = 0
    private var sleepingTime: Long = 0
    private var notificationMessage: String = ""
    private var notificationFrequency: Int = 0
    private var currentToneUri: String? = ""
    private lateinit var bs_wr_weight: TextInputEditText
    private lateinit var bs_wr_workout: TextInputEditText
    private lateinit var bs_wr_wakeuptime: TextInputEditText
    private lateinit var bs_wr_sleeptime: TextInputEditText
    private lateinit var bs_wr_notifications_message: TextInputEditText
    private lateinit var bs_wr_target: TextInputEditText
    private lateinit var bs_wr_notification_tone: TextInputEditText
    private lateinit var bs_wr_timeradiogroup: RadioGroup
    private lateinit var btn_bs_wr_continue: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_bottomsheet_dialog_water_remainder, container, false
        )
        bs_wr_weight = view.findViewById(R.id.bs_wr_weight)
        bs_wr_workout = view.findViewById(R.id.bs_wr_workout)
        bs_wr_wakeuptime = view.findViewById(R.id.bs_wr_wakeuptime)
        bs_wr_sleeptime = view.findViewById(R.id.bs_wr_sleeptime)
        bs_wr_target = view.findViewById(R.id.bs_wr_target)
        bs_wr_notifications_message = view.findViewById(R.id.bs_wr_notifications_message)
        bs_wr_notification_tone = view.findViewById(R.id.bs_wr_notification_tone)
        bs_wr_timeradiogroup = view.findViewById(R.id.bs_wr_timeradiogroup)
        btn_bs_wr_continue = view.findViewById(R.id.btn_bs_wr_continue)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPreferences =
            context.getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
        bs_wr_weight.setText("" + sharedPreferences.getInt(AppUnits.WEIGHT_KEY, 0))
        bs_wr_workout.setText("" + sharedPreferences.getInt(AppUnits.WORK_TIME_KEY, 0))
        bs_wr_target.setText("" + sharedPreferences.getInt(AppUnits.TOTAL_INTAKE, 0))
        bs_wr_notifications_message.setText(
            "" + sharedPreferences.getString(
                AppUnits.NOTIFICATION_MSG_KEY, "Hey... Lets drink some water...."
            )
        )
        currentToneUri = sharedPreferences.getString(
            NOTIFICATION_TONE_URI_KEY,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
        )
        bs_wr_notification_tone.setText(
            RingtoneManager.getRingtone(
                context, Uri.parse(currentToneUri)
            ).getTitle(context)
        )

        bs_wr_timeradiogroup.setOnCheckedChangeListener { _, position ->
            notificationFrequency = when (position) {
                0 -> 30
                1 -> 45
                2 -> 60
                else -> 30
            }
        }
        notificationFrequency = sharedPreferences.getInt(AppUnits.NOTIFICATION_FREQUENCY_KEY, 30)
        when (notificationFrequency) {
            30 -> bs_wr_timeradiogroup.check(R.id.bs_wr_30timeradio)
            45 -> bs_wr_timeradiogroup.check(R.id.bs_wr_45timeradio)
            60 -> bs_wr_timeradiogroup.check(R.id.bs_wr_60timeradio)
            else -> {
                bs_wr_timeradiogroup.check(R.id.bs_wr_30timeradio)
                notificationFrequency = 30
            }
        }

        bs_wr_notification_tone.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            intent.putExtra(
                RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notification"
            )
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentToneUri)
            startActivityForResult(intent, 999)
        }
        wakeupTime = sharedPreferences.getLong(AppUnits.WAKEUP_TIME, 1558323000000)
        sleepingTime =
            sharedPreferences.getLong(AppUnits.SLEEPING_TIME_KEY, 1558369800000)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = wakeupTime
        bs_wr_wakeuptime.setText(
            String.format(
                "%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )
        calendar.timeInMillis = sleepingTime
        bs_wr_sleeptime.setText(
            String.format(
                "%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )

        bs_wr_wakeuptime.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = wakeupTime
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                    calendar.set(Calendar.MINUTE, selectedMinute)
                    wakeupTime = calendar.timeInMillis
                    bs_wr_wakeuptime.setText(
                        String.format(
                            "%02d:%02d",
                            selectedHour,
                            selectedMinute
                        )
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            mTimePicker.setTitle("Select wakeup Time")
            mTimePicker.show()
        }

        bs_wr_sleeptime.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sleepingTime
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                    calendar.set(Calendar.MINUTE, selectedMinute)
                    sleepingTime = calendar.timeInMillis
                    bs_wr_sleeptime.setText(
                        String.format(
                            "%02d:%02d",
                            selectedHour,
                            selectedMinute
                        )
                    )
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            mTimePicker.setTitle("Select sleeping Time")
            mTimePicker.show()
        }

        btn_bs_wr_continue.setOnClickListener {
            val currenTarget = sharedPreferences.getInt(AppUnits.TOTAL_INTAKE, 0)
            weight = bs_wr_weight.text.toString()
            workTime = bs_wr_wakeuptime.text.toString()
            notificationMessage = bs_wr_notifications_message.text.toString()
            customTarget = bs_wr_target.text.toString()


            val editor = sharedPreferences.edit()
            editor.putInt(AppUnits.WEIGHT_KEY, weight.toInt())
            editor.putInt(AppUnits.WORK_TIME_KEY, workTime.toInt())
            editor.putLong(AppUnits.WAKEUP_TIME, wakeupTime)
            editor.putLong(AppUnits.SLEEPING_TIME_KEY, sleepingTime)
            editor.putString(AppUnits.NOTIFICATION_MSG_KEY, notificationMessage)
            editor.putInt(AppUnits.NOTIFICATION_FREQUENCY_KEY, notificationFrequency)

            val sqliteHelper = SqliteHelper(context)

            if (currenTarget != customTarget.toInt()) {
                editor.putInt(AppUnits.TOTAL_INTAKE, customTarget.toInt())
                sqliteHelper.updateTotalIntake(AppUnits.getCurrentDate()!!, customTarget.toInt())
            } else {
                val totalIntake = AppUnits.calculateIntake(weight.toInt(), workTime.toInt())
                val df = DecimalFormat("#")
                df.roundingMode = RoundingMode.CEILING
                editor.putInt(AppUnits.TOTAL_INTAKE, df.format(totalIntake).toInt())

                sqliteHelper.updateTotalIntake(
                    AppUnits.getCurrentDate()!!,
                    df.format(totalIntake).toInt()
                )
            }
            editor.apply()
            Toast.makeText(context, "Values updated successfully", Toast.LENGTH_SHORT).show()
            val alarmHelper = AlarmHelper()
            alarmHelper.cancelAlarm(context)
            alarmHelper.setAlarm(
                context,
                sharedPreferences.getInt(AppUnits.NOTIFICATION_FREQUENCY_KEY, 30).toLong()
            )
            dismiss()
            (activity as Water_Remainder?)!!.updateValues()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && resultCode == 999) {
            val uri =
                (data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) as Uri).also {
                    currentToneUri = it.toString()
                }
            sharedPreferences.edit().putString(AppUnits.NOTIFICATION_TONE_URI_KEY, currentToneUri)
                .apply()
            val ringtone = RingtoneManager.getRingtone(context, uri)
            bs_wr_notification_tone.setText(ringtone.getTitle(context))
        }
    }

}