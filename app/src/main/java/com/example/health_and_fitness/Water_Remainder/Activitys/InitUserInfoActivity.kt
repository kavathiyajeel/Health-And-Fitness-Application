package com.example.health_and_fitness.Water_Remainder.Activitys

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import com.example.health_and_fitness.Activitys.Water_Remainder
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Water_Remainder.AppUnits
import com.example.health_and_fitness.databinding.ActivityBasalMetabolicRateBinding
import com.example.health_and_fitness.databinding.ActivityInitUserInfoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Calendar

class InitUserInfoActivity : AppCompatActivity() {
    private var weight: String = ""
    private var workTime: String = ""
    private var wakeupTime: Long = 0
    private var sleepingTime: Long = 0
    private lateinit var sharedPref: SharedPreferences
    private var doubleBackToExitPressedOnce = false
    private lateinit var wr_weight: TextInputEditText
    private lateinit var wr_workout: TextInputEditText
    private lateinit var wr_wakeuptime: TextInputEditText
    private lateinit var wr_sleeptime: TextInputEditText
    private lateinit var wr_weight_field: TextInputLayout
    private lateinit var wr_workout_field: TextInputLayout
    private lateinit var btn_continue_info: Button
    private lateinit var binding: ActivityInitUserInfoBinding
    private lateinit var init_user_info_parent_layout: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val is24h = android.text.format.DateFormat.is24HourFormat(this.applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_init_user_info)
        //binding = ActivityInitUserInfoBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "User Info"

        wr_weight = findViewById(R.id.wr_weight)
        wr_workout = findViewById(R.id.wr_workout)
        wr_wakeuptime = findViewById(R.id.wr_wakeuptime)
        wr_sleeptime = findViewById(R.id.wr_sleeptime)
        wr_weight_field = findViewById(R.id.wr_weight_field)
        wr_workout_field = findViewById(R.id.wr_workout_field)
        btn_continue_info = findViewById(R.id.btn_continue_info)
        init_user_info_parent_layout = findViewById(R.id.init_user_info_parent_layout)

        sharedPref = getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
        wakeupTime = sharedPref.getLong(AppUnits.WAKEUP_TIME, 1558323000000)
        sleepingTime = sharedPref.getLong(AppUnits.SLEEPING_TIME_KEY, 1558369800000)

        wr_wakeuptime.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = wakeupTime

            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    wakeupTime = time.timeInMillis

                    wr_wakeuptime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            mTimePicker.setTitle("Select Wakeup Time")
            mTimePicker.show()
        }
        wr_sleeptime.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sleepingTime

            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, selectedHour)
                    time.set(Calendar.MINUTE, selectedMinute)
                    sleepingTime = time.timeInMillis

                    wr_sleeptime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            mTimePicker.setTitle("Select Sleeping Time")
            mTimePicker.show()
        }
        /*
                btn_continue_info.setOnClickListener {
                    if (vaildateInitUserInfo()){
                        val editor = sharedPref.edit()
                        editor.putInt(AppUnits.WEIGHT_KEY,weight.toInt())
                        editor.putInt(AppUnits.WORK_TIME_KEY,workTime.toInt())
                        editor.putLong(AppUnits.WAKEUP_TIME,wakeupTime)
                        editor.putLong(AppUnits.SLEEPING_TIME_KEY,sleepingTime)
                        editor.putBoolean(AppUnits.FIRST_RUN_KEY,false)

                        val totalIntake = AppUnits.calculateIntake(weight.toInt(),workTime.toInt())
                        val df = DecimalFormat("#")
                        df.roundingMode = RoundingMode.CEILING
                        editor.putInt(AppUnits.TOTAL_INTAKE,df.format(totalIntake).toInt())
                        editor.apply()
                        startActivity(Intent(this,Water_Remainder::class.java))
                        finish()
                    }
                }*/
        btn_continue_info.setOnClickListener {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(init_user_info_parent_layout.windowToken, 0)

            weight = wr_weight.text.toString()
            workTime = wr_workout.text.toString()

            when {

                TextUtils.isEmpty(weight) -> Snackbar.make(
                    it,
                    "Please input your weight",
                    Snackbar.LENGTH_SHORT
                ).show()

                weight.toInt() > 200 || weight.toInt() < 20 -> Snackbar.make(
                    it,
                    "Please input a valid weight",
                    Snackbar.LENGTH_SHORT
                ).show()

                TextUtils.isEmpty(workTime) -> Snackbar.make(
                    it,
                    "Please input your workout time",
                    Snackbar.LENGTH_SHORT
                ).show()

                workTime.toInt() > 500 || workTime.toInt() < 0 -> Snackbar.make(
                    it,
                    "Please input a valid workout time",
                    Snackbar.LENGTH_SHORT
                ).show()

                else -> {

                    val editor = sharedPref.edit()
                    editor.putInt(AppUnits.WEIGHT_KEY, weight.toInt())
                    editor.putInt(AppUnits.WORK_TIME_KEY, workTime.toInt())
                    editor.putLong(AppUnits.WAKEUP_TIME, wakeupTime)
                    editor.putLong(AppUnits.SLEEPING_TIME_KEY, sleepingTime)
                    editor.putBoolean(AppUnits.FIRST_RUN_KEY, false)

                    val totalIntake = AppUnits.calculateIntake(weight.toInt(), workTime.toInt())
                    val df = DecimalFormat("#")
                    df.roundingMode = RoundingMode.CEILING
                    editor.putInt(AppUnits.TOTAL_INTAKE, df.format(totalIntake).toInt())

                    editor.apply()
                    startActivity(Intent(this, Water_Remainder::class.java))
                    finish()

                }
            }
        }


        /*private fun vaildateInitUserInfo(): Boolean {
            val weight = binding.wrWeight.text.toString()
            val worktime = binding.wrWorkout.text.toString()
            if (binding.wrWeight.text.toString().isEmpty()) {
                binding.wrWeightField.error = "Weight is required"
                binding.wrWeight.requestFocus()
                return false
            } else if (weight > 200 || weight < 20) {
                binding.wrWeightField.error = "Enter the valid Weight"
                binding.wrWeight.requestFocus()
            } else {
                binding.wrWeightField.isErrorEnabled = false
            }

            if (binding.wrWorkout.text.toString().isEmpty()) {
                binding.wrWorkoutField.error = "Workout Time is required"
                binding.wrWorkout.requestFocus()
                return false
            } else if (worktime > 500 || worktime < 0) {
                binding.wrWorkoutField.error = "Enter the valid Workout Time"
                binding.wrWorkout.requestFocus()
            } else {
                binding.wrWorkoutField.isErrorEnabled = false
            }
            return true
        }*/
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
}