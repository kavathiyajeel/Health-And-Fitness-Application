package com.example.health_and_fitness.Medicine_Remainder.Activitys

import android.annotation.SuppressLint
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


class Add_Medicine : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var txt_am_mname: TextInputEditText
    private lateinit var card_am_date: CardView
    private lateinit var card_am_time: CardView
    private lateinit var card_am_type_repetitions: CardView
    private lateinit var card_am_repetition_interval: CardView
    private lateinit var txt_am_date: TextView
    private lateinit var txt_am_time: TextView
    private lateinit var txt_am_repeat: TextView
    private lateinit var txt_am_repetition_interval: TextView
    private lateinit var txt_am_type_repetition: TextView
    private lateinit var txt_am_notification_mode: TextView
    private lateinit var btn_am_off_notification: FloatingActionButton
    private lateinit var btn_am_on_notification: FloatingActionButton
    private lateinit var btn_add_medication: Button
    private lateinit var switch_am_repeat: SwitchMaterial
    private lateinit var mCalendar: Calendar
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mDay: Int = 0
    private var mTitle: String? = null
    private var mTime: String? = null
    private var mDate: String? = null
    private var mRepeat: String? = null
    private var mRepeatNo: String? = null
    private var mRepeatType: String? = null
    private var mActive: String? = null

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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicine)

        /// Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Add Medicine Remainder"


        card_am_date = findViewById(R.id.card_am_date)
        card_am_time = findViewById(R.id.card_am_time)
        card_am_type_repetitions = findViewById(R.id.card_am_type_repetitions)
        card_am_repetition_interval = findViewById(R.id.card_am_repetition_interval)
        txt_am_mname = findViewById(R.id.txt_am_mname)
        txt_am_date = findViewById(R.id.txt_am_date)
        txt_am_time = findViewById(R.id.txt_am_time)
        txt_am_repeat = findViewById(R.id.txt_am_repeat)
        txt_am_repetition_interval = findViewById(R.id.txt_am_repetition_interval)
        txt_am_type_repetition = findViewById(R.id.txt_am_type_repetition)
        txt_am_notification_mode = findViewById(R.id.txt_am_notification_mode)
        btn_am_off_notification = findViewById(R.id.btn_am_off_notification)
        btn_am_on_notification = findViewById(R.id.btn_am_on_notification)
        btn_add_medication = findViewById(R.id.btn_add_medication)
        switch_am_repeat = findViewById(R.id.switch_am_repeat)


        // Initialize default values
        mActive = "true"
        mRepeat = "true"
        mRepeatNo = "1"
        mRepeatType = "Hour"

        mCalendar = Calendar.getInstance()
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        mMinute = mCalendar.get(Calendar.MINUTE)
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH) + 1
        mDay = mCalendar.get(Calendar.DATE)

        mDate = "$mDay/$mMonth/$mYear"
        mTime = "$mHour:$mMinute"


        //medicine title
        txt_am_mname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mTitle = charSequence.toString().trim()
                txt_am_mname.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        txt_am_date.text = mDate
        txt_am_time.text = mTime
        txt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
        txt_am_repetition_interval.text = mRepeatNo
        txt_am_type_repetition.text = mRepeatType

        // To save state on device rotation
        if (savedInstanceState != null) {
            val savedTitle = savedInstanceState.getString(KEY_TITLE)
            txt_am_mname.setText(savedTitle)
            mTitle = savedTitle

            val savedTime = savedInstanceState.getString(KEY_TIME)
            txt_am_time.text = savedTime
            mTime = savedTime

            val savedDate = savedInstanceState.getString(KEY_DATE)
            txt_am_date.text = savedDate
            mDate = savedDate

            val saveRepeat = savedInstanceState.getString(KEY_REPEAT)
            txt_am_repeat.text = saveRepeat
            mRepeat = saveRepeat

            val savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO)
            txt_am_repetition_interval.text = savedRepeatNo
            mRepeatNo = savedRepeatNo

            val savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE)
            txt_am_type_repetition.text = savedRepeatType
            mRepeatType = savedRepeatType

            mActive = savedInstanceState.getString(KEY_ACTIVE)
        }


        //medicine date
        card_am_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme,
                { _, year, month, dayOfMonth ->
                    mDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year)
                    txt_am_date.text = mDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            )

            datePicker.show()
        }
        card_am_time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,R.style.CustomTimePickerDialogTheme,
                { _, selectedHour, selectedMinute ->
                    // Handle the selected time here
                    mTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    txt_am_time.text = mTime
                    // Update your UI with the selected time
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        // Setup up active buttons
        if (mActive.equals("false")) {
            btn_am_off_notification.visibility = View.VISIBLE
            btn_am_on_notification.visibility = View.GONE
        } else if (mActive.equals("true")) {
            btn_am_on_notification.visibility = View.VISIBLE
            btn_am_off_notification.visibility = View.GONE
        }

        // On clicking the inactive button
        btn_am_off_notification.setOnClickListener {
            btn_am_off_notification.visibility = View.GONE
            btn_am_on_notification.visibility = View.VISIBLE
            mActive = "true"
            txt_am_notification_mode.text = "On"
        }
        // On clicking the active button
        btn_am_on_notification.setOnClickListener {
            btn_am_on_notification.visibility = View.GONE
            btn_am_off_notification.visibility = View.VISIBLE
            mActive = "false"
            txt_am_notification_mode.text = "Off"
        }

        // On clicking the repeat switch
        switch_am_repeat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mRepeat = "true"
                txt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
            } else {
                mRepeat = "false"
                txt_am_repeat.setText(R.string.repeat_off)
            }
        }

        card_am_repetition_interval.setOnClickListener {
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
                txt_am_repetition_interval.text = mRepeatNo
                txt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
                alert.dismiss()
            }

            alert.show()
        }

        card_am_type_repetitions.setOnClickListener {
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
                txt_am_type_repetition.text = mRepeatType
                txt_am_repeat.text = "Every $mRepeatNo $mRepeatType(s)"
                alert.dismiss()
            }
            alert.show()
        }

        btn_add_medication.setOnClickListener {

            if (txt_am_mname.text?.isEmpty() == true) {
                Toast.makeText(applicationContext, "Medicine name is required.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val remainderDatabase = RemainderDatabase(this)
                val ID = remainderDatabase.addRemainder(
                    Reminder(
                        0,
                        mTitle,
                        mDate,
                        mTime,
                        mRepeat,
                        mRepeatNo,
                        mRepeatType,
                        mActive
                    )
                )

                mCalendar.apply {
                    set(Calendar.MONTH, mMonth - 1)
                    set(Calendar.YEAR, mYear)
                    set(Calendar.DAY_OF_MONTH, mDay)
                    set(Calendar.HOUR_OF_DAY, mHour)
                    set(Calendar.MINUTE, mMinute)
                    set(Calendar.SECOND, 0)

                }

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
                            ID,
                            mRepeatTime
                        )
                    } else if (mRepeat == "false") {
                        AlarmReceiver().setAlarm(applicationContext, mCalendar, ID)
                    }
                }
                Toast.makeText(
                    applicationContext,
                    "Medicine Remainder is Added.",
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

        outState.putCharSequence(KEY_TITLE, txt_am_mname.text)
        outState.putCharSequence(KEY_TIME, txt_am_time.text)
        outState.putCharSequence(KEY_DATE, txt_am_date.text)
        outState.putCharSequence(KEY_REPEAT, txt_am_repeat.text)
        outState.putCharSequence(KEY_REPEAT_NO, txt_am_repetition_interval.text)
        outState.putCharSequence(KEY_REPEAT_TYPE, txt_am_type_repetition.text)
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
        mTime = if (minute < 10 || hourOfDay < 10) {
            "$hourOfDay:0$minute"
        } else {
            "$hourOfDay:$minute"
        }
        txt_am_time.text = mTime
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val updatedMonthOfYear = monthOfYear + 1
        mDay = dayOfMonth
        mMonth = updatedMonthOfYear
        mYear = year
        mDate = "$dayOfMonth/$updatedMonthOfYear/$year"
        txt_am_date.text = mDate
    }
}