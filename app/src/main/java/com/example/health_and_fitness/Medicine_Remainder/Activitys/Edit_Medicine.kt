package com.example.health_and_fitness.Medicine_Remainder.Activitys

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.health_and_fitness.Activitys.Medicine_Remainder
import com.example.health_and_fitness.Medicine_Remainder.AlarmReceiver
import com.example.health_and_fitness.Medicine_Remainder.Database.RemainderDatabase
import com.example.health_and_fitness.Medicine_Remainder.Model.Reminder
import com.example.health_and_fitness.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class Edit_Medicine : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var etxt_am_mname: TextInputEditText
    private lateinit var ecard_am_date: CardView
    private lateinit var ecard_am_time: CardView
    private lateinit var ecard_am_type_repetitions: CardView
    private lateinit var ecard_am_repetition_interval: CardView
    private lateinit var etxt_am_date: TextView
    private lateinit var etxt_am_time: TextView
    private lateinit var etxt_am_repeat: TextView
    private lateinit var etxt_am_repetition_interval: TextView
    private lateinit var etxt_am_type_repetition: TextView
    private lateinit var etxt_am_notification_mode: TextView
    private lateinit var ebtn_am_off_notification: FloatingActionButton
    private lateinit var ebtn_am_on_notification: FloatingActionButton
    private lateinit var ebtn_update_medication: Button
    private lateinit var eswitch_am_repeat: SwitchMaterial
    private var mTitle: String? = null
    private var mTime: String? = null
    private var mDate: String? = null
    private var mRepeatNo: String? = null
    private var mRepeatType: String? = null
    private var mActive: String? = null
    private var mRepeat: String? = null
    private var mReceivedID: Int = 0
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mDay: Int = 0
    private lateinit var mReceivedReminder: Reminder
    private lateinit var remainderDatabase: RemainderDatabase
    private lateinit var mAlarmReceiver: AlarmManager

    // Values for orientation change
    private val KEY_TITLE = "title_key"
    private val KEY_TIME = "time_key"
    private val KEY_DATE = "date_key"
    private val KEY_REPEAT = "repeat_key"
    private val KEY_REPEAT_NO = "repeat_no_key"
    private val KEY_REPEAT_TYPE = "repeat_type_key"
    private val KEY_ACTIVE = "active_key"

    // Constant values in milliseconds
    private val milMinute: Long = 60000L
    private val milHour: Long = 3600000L
    private val milDay: Long = 86400000L
    private val milWeek: Long = 604800000L
    private val milMonth: Long = 2592000000L

    companion object {
        const val EXTRA_REMINDER_ID = "Reminder_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medicine)

        /// Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Edit Medicine Remainder"


        ecard_am_date = findViewById(R.id.ecard_am_date)
        ecard_am_time = findViewById(R.id.ecard_am_time)
        ecard_am_type_repetitions = findViewById(R.id.ecard_am_type_repetitions)
        ecard_am_repetition_interval = findViewById(R.id.ecard_am_repetition_interval)
        etxt_am_mname = findViewById(R.id.etxt_am_mname)
        etxt_am_date = findViewById(R.id.etxt_am_date)
        etxt_am_time = findViewById(R.id.etxt_am_time)
        etxt_am_repeat = findViewById(R.id.etxt_am_repeat)
        etxt_am_repetition_interval = findViewById(R.id.etxt_am_repetition_interval)
        etxt_am_type_repetition = findViewById(R.id.etxt_am_type_repetition)
        etxt_am_notification_mode = findViewById(R.id.etxt_am_notification_mode)
        ebtn_am_off_notification = findViewById(R.id.ebtn_am_off_notification)
        ebtn_am_on_notification = findViewById(R.id.ebtn_am_on_notification)
        ebtn_update_medication = findViewById(R.id.ebtn_update_medication)
        eswitch_am_repeat = findViewById(R.id.eswitch_am_repeat)

        mReceivedID = intent.getIntExtra(EXTRA_REMINDER_ID, 0)
        remainderDatabase = RemainderDatabase(this)
        mReceivedReminder = remainderDatabase.getRemainder(mReceivedID)

        etxt_am_mname.setText(mReceivedReminder.title)
        etxt_am_date.text = mReceivedReminder.date
        etxt_am_time.text = mReceivedReminder.time
        etxt_am_repetition_interval.text = mReceivedReminder.repeatNo
        etxt_am_type_repetition.text = mReceivedReminder.repeatType

        if (mReceivedReminder.repeat == "false") {
            eswitch_am_repeat.isChecked = false
            etxt_am_repeat.text = getString(R.string.repeat_off)
        } else if (mReceivedReminder.repeat == "true") {
            etxt_am_repeat.text =
                getString(
                    R.string.repeat_format,
                    mReceivedReminder.repeatNo,
                    mReceivedReminder.repeatType
                )
            eswitch_am_repeat.isChecked = true
        }

        if (mReceivedReminder.active == "false") {
            etxt_am_notification_mode.text = getString((R.string.off))
            ebtn_am_on_notification.visibility = View.GONE
            ebtn_am_off_notification.visibility = View.VISIBLE
        } else if (mReceivedReminder.active == "true") {
            etxt_am_notification_mode.text = getString(R.string.on)
            ebtn_am_off_notification.visibility = View.GONE
            ebtn_am_on_notification.visibility = View.VISIBLE
        }

        //medicine title
        etxt_am_mname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mTitle = charSequence.toString().trim()
                etxt_am_mname.error = null

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        // To save state on device rotation
        if (savedInstanceState != null) {
            val savedTitle = savedInstanceState.getString(KEY_TITLE)
            etxt_am_mname.setText(savedTitle)
            mTitle = savedTitle


            val savedTime = savedInstanceState.getString(KEY_TIME)
            etxt_am_time.text = savedTime
            mTime = savedTime


            val savedDate = savedInstanceState.getString(KEY_DATE)
            etxt_am_date.text = savedDate
            mDate = savedDate


            val saveRepeat = savedInstanceState.getString(KEY_REPEAT)
            etxt_am_repeat.text = saveRepeat
            mRepeat = saveRepeat


            val savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO)
            etxt_am_repetition_interval.text = savedRepeatNo
            mRepeatNo = savedRepeatNo


            val savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE)
            etxt_am_type_repetition.text = savedRepeatType
            mRepeatType = savedRepeatType


            mActive = savedInstanceState.getString(KEY_ACTIVE)
        }

        // Setup up active buttons
        /*if (mActive.equals("false")) {
            ebtn_am_off_notification.visibility = View.VISIBLE
            ebtn_am_on_notification.visibility = View.GONE
        } else if (mActive.equals("true")) {
            ebtn_am_on_notification.visibility = View.VISIBLE
            ebtn_am_off_notification.visibility = View.GONE
        }*/
        // Setup repeat switch
        if (mRepeat == "false") {
            eswitch_am_repeat.isChecked = false
            etxt_am_repeat.text = getString(R.string.repeat_off)
        } else if (mRepeat == "true") {
            eswitch_am_repeat.isChecked = true
        }

        if (mActive == "false") {
            ebtn_am_off_notification.visibility = View.VISIBLE
            ebtn_am_on_notification.visibility = View.GONE
            etxt_am_notification_mode.text = getString(R.string.off)
        } else if (mActive == "true") {
            ebtn_am_on_notification.visibility = View.VISIBLE
            ebtn_am_off_notification.visibility = View.GONE
            etxt_am_notification_mode.text = getString(R.string.on)
        }

        // Obtain Date and Time details
        val mCalendar = Calendar.getInstance()
        val mAlarmReceiver = AlarmReceiver()

        /*val mDateSplit = mDate!!.split("/")
        val mTimeSplit = mTime!!.split(":")

        val mDay = mDateSplit[0].toInt()
        val mMonth = mDateSplit[1].toInt()
        val mYear = mDateSplit[2].toInt()
        val mHour = mTimeSplit[0].toInt()
        val mMinute = mTimeSplit[1].toInt()*/

        ecard_am_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme,
                { _, year, month, dayOfMonth ->
                    mDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year)
                    etxt_am_date.text = mDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            )
            datePicker.show()
        }
        ecard_am_time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    // Handle the selected time here
                    mTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    etxt_am_time.text = mTime
                    // Update your UI with the selected time
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }
        // On clicking the inactive button
        ebtn_am_off_notification.setOnClickListener {
            ebtn_am_off_notification.visibility = View.GONE
            ebtn_am_on_notification.visibility = View.VISIBLE
            mActive = "true"
            etxt_am_notification_mode.text = getString(R.string.on)
        }
        // On clicking the active button
        ebtn_am_on_notification.setOnClickListener {
            ebtn_am_on_notification.visibility = View.GONE
            ebtn_am_off_notification.visibility = View.VISIBLE
            mActive = "false"
            etxt_am_notification_mode.text = getString(R.string.off)
        }
        // On clicking the repeat switch
        eswitch_am_repeat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mRepeat = "true"
                etxt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
            } else {
                mRepeat = "false"
                etxt_am_repeat.setText(R.string.repeat_off)
            }
        }
        ecard_am_repetition_interval.setOnClickListener {
            val customDialogView = layoutInflater.inflate(R.layout.repeatno_custom_dialog, null)
            val txt_repeatno_dialog =
                customDialogView.findViewById<TextInputEditText>(R.id.txt_repeatno_dialog)
            val btn_repeatno_cancel =
                customDialogView.findViewById<Button>(R.id.btn_repeatno_cancel)
            val btn_repeatno_ok = customDialogView.findViewById<Button>(R.id.btn_repeatno_ok)

            val alert = AlertDialog.Builder(this)
                .setView(customDialogView)
                .setCancelable(false)
                .create()
            alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            txt_repeatno_dialog.inputType = InputType.TYPE_CLASS_NUMBER
            btn_repeatno_cancel.setOnClickListener { alert.dismiss() }
            btn_repeatno_ok.setOnClickListener {
                val inputText = txt_repeatno_dialog.text.toString()
                mRepeatNo = if (inputText.isEmpty()) {
                    "1"
                } else {
                    inputText.trim()
                }
                etxt_am_repetition_interval.text = mRepeatNo
                etxt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
                alert.dismiss()
            }

            alert.show()
        }

        ecard_am_type_repetitions.setOnClickListener {
            val items = arrayOf("Minute", "Hour", "Day", "Week", "Month")
            val customDialogView = layoutInflater.inflate(R.layout.repeattype_custom_dialog, null)
            val alert = AlertDialog.Builder(this)
                .setView(customDialogView)
                .setCancelable(false)
                .create()

            alert.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val list_repeattype_dialog =
                customDialogView.findViewById<ListView>(R.id.list_repeattype_dialog)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
            list_repeattype_dialog.adapter = adapter

            list_repeattype_dialog.setOnItemClickListener { _, _, position, _ ->
                val selectedType = items[position]
                mRepeatType = selectedType
                etxt_am_type_repetition.text = mRepeatType
                etxt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
                alert.dismiss()
            }
            alert.show()
        }
        ebtn_update_medication.setOnClickListener {

            if (etxt_am_mname.text?.isEmpty() == true) {
                Toast.makeText(applicationContext, "Medicine name is required.", Toast.LENGTH_SHORT)
                    .show()
            } else {

                mReceivedReminder.title = mTitle
                mReceivedReminder.date = mDate
                mReceivedReminder.time = mTime
                mReceivedReminder.repeat = mRepeat
                mReceivedReminder.repeatNo = mRepeatNo
                mReceivedReminder.repeatType = mRepeatType
                mReceivedReminder.active = mActive

                remainderDatabase.updateRemainder(mReceivedReminder)

                mCalendar.set(Calendar.MONTH, mMonth - 1)
                mCalendar.set(Calendar.YEAR, mYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay)
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour)
                mCalendar.set(Calendar.MINUTE, mMinute)
                mCalendar.set(Calendar.SECOND, 0)

                mAlarmReceiver.cancelAlarm(applicationContext, mReceivedID)

                val mRepeatTime: Long = when (mRepeatType) {
                    "Minute" -> Integer.parseInt(mRepeatNo) * milMinute
                    "Hour" -> Integer.parseInt(mRepeatNo) * milHour
                    "Day" -> Integer.parseInt(mRepeatNo) * milDay
                    "Week" -> Integer.parseInt(mRepeatNo) * milWeek
                    "Month" -> Integer.parseInt(mRepeatNo) * milMonth
                    else -> 0
                }

                if (mActive == "true") {
                    if (mRepeat == "true") {
                        AlarmReceiver().setRepeatAlarm(
                            applicationContext,
                            mCalendar,
                            mReceivedID,
                            mRepeatTime
                        )
                    } else if (mRepeat == "false") {
                        AlarmReceiver().setAlarm(applicationContext, mCalendar, mReceivedID)
                    }
                }
                Toast.makeText(
                    applicationContext,
                    "Medicine Remainder is Updated.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, Medicine_Remainder::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putCharSequence(KEY_TITLE, etxt_am_mname.text)
        outState.putCharSequence(KEY_TIME, etxt_am_time.text)
        outState.putCharSequence(KEY_DATE, etxt_am_date.text)
        outState.putCharSequence(KEY_REPEAT, etxt_am_repeat.text)
        outState.putCharSequence(KEY_REPEAT_NO, etxt_am_repetition_interval.text)
        outState.putCharSequence(KEY_REPEAT_TYPE, etxt_am_type_repetition.text)
        outState.putCharSequence(KEY_ACTIVE, mActive)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle the back button click event
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
        mHour = hourOfDay
        mMinute = minute
        mTime = if (minute < 10) {
            "$hourOfDay:0$minute"
        } else {
            "$hourOfDay:$minute"
        }
        etxt_am_time.text = mTime
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val updatedMonthOfYear = monthOfYear + 1
        mDay = dayOfMonth
        mMonth = updatedMonthOfYear
        mYear = year
        mDate = "$dayOfMonth/$updatedMonthOfYear/$year"
        etxt_am_date.text = mDate
    }
}