package com.example.health_and_fitness.Activitys

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.TypedValue
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Water_Remainder.Activitys.InitUserInfoActivity
import com.example.health_and_fitness.Water_Remainder.AppUnits
import com.example.health_and_fitness.Water_Remainder.Fragment.Bottomsheet_dialog_water_remainder
import com.example.health_and_fitness.Water_Remainder.Helper.AlarmHelper
import com.example.health_and_fitness.Water_Remainder.Helper.SqliteHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class Water_Remainder : AppCompatActivity() {

    private var totalIntake: Int = 0
    private var inTook: Int = 0
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private lateinit var dateNow: String
    private var notificStatus: Boolean = false
    private var selectedOption: Int? = null
    private lateinit var txt_wr_Totalintake: TextView
    private lateinit var txt_wr_Intook: TextView
    private lateinit var wr_intake_progress: LinearProgressIndicator
    private lateinit var btn_wr_notification: FloatingActionButton
    private lateinit var btn_wr_setteings: FloatingActionButton
    private lateinit var btn_wr_add: FloatingActionButton
    private lateinit var txt_wr_custom: TextView
    private lateinit var card_wr_50ml: MaterialCardView
    private lateinit var card_wr_100ml: MaterialCardView
    private lateinit var card_wr_150ml: MaterialCardView
    private lateinit var card_wr_200ml: MaterialCardView
    private lateinit var card_wr_250ml: MaterialCardView
    private lateinit var card_wr_custom: MaterialCardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_remainder)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Water Remainder"

        txt_wr_Intook = findViewById(R.id.txt_wr_Intook)
        txt_wr_Totalintake = findViewById(R.id.txt_wr_Totalintake)
        wr_intake_progress = findViewById(R.id.wr_intake_progress)
        btn_wr_notification = findViewById(R.id.btn_wr_notification)
        btn_wr_setteings = findViewById(R.id.btn_wr_setteings)
        btn_wr_add = findViewById(R.id.btn_wr_add)
        txt_wr_custom = findViewById(R.id.txt_wr_custom)
        card_wr_50ml = findViewById(R.id.card_wr_50ml)
        card_wr_100ml = findViewById(R.id.card_wr_100ml)
        card_wr_150ml = findViewById(R.id.card_wr_150ml)
        card_wr_200ml = findViewById(R.id.card_wr_200ml)
        card_wr_250ml = findViewById(R.id.card_wr_250ml)
        card_wr_custom = findViewById(R.id.card_wr_custom)

        sharedPref = getSharedPreferences(AppUnits.USERS_SHARED_PREF, AppUnits.PRIVATE_MODE)
        sqliteHelper = SqliteHelper(this)

        totalIntake = sharedPref.getInt(AppUnits.TOTAL_INTAKE, 0)

        dateNow = AppUnits.getCurrentDate()!!

        if (totalIntake <= 0) {
            startActivity(Intent(this, InitUserInfoActivity::class.java))
            finish()
        }

        val outValue = TypedValue()
        applicationContext.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )

        notificStatus = sharedPref.getBoolean(AppUnits.NOTIFICATION_STATUS_KEY, true)
        val alarm = AlarmHelper()
        if (!alarm.checkAlarm(this) && notificStatus) {
            btn_wr_notification.setImageDrawable(getDrawable(R.drawable.ic_on_notification))
            alarm.setAlarm(
                this,
                sharedPref.getInt(AppUnits.NOTIFICATION_FREQUENCY_KEY, 30).toLong()
            )
        }

        if (notificStatus) {
            btn_wr_notification.setImageDrawable(getDrawable(R.drawable.ic_on_notification))
        } else {
            btn_wr_notification.setImageDrawable(getDrawable(R.drawable.ic_off_notification))
        }

        sqliteHelper.addAll(dateNow, 0, totalIntake)

        updateValues()

        btn_wr_setteings.setOnClickListener {
            val bottomSheetFragment = Bottomsheet_dialog_water_remainder(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        btn_wr_add.setOnClickListener {
            if (selectedOption != null) {
                if ((inTook * 100 / totalIntake) < 140) {
                    if (sqliteHelper.addIntook(dateNow, selectedOption!!) > 0) {
                        inTook += selectedOption!!
                        setWaterLevel(inTook, totalIntake)
                        Toast.makeText(this, "Your water intake was saved", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "You already achieved the goal", Toast.LENGTH_SHORT)
                            .show()
                    }
                    selectedOption = null
                    txt_wr_custom.text = "Custom"
                    card_wr_50ml.background = getDrawable(outValue.resourceId)
                    card_wr_100ml.background = getDrawable(outValue.resourceId)
                    card_wr_150ml.background = getDrawable(outValue.resourceId)
                    card_wr_200ml.background = getDrawable(outValue.resourceId)
                    card_wr_250ml.background = getDrawable(outValue.resourceId)
                } else {
                    Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_wr_notification.setOnClickListener {
            notificStatus = !notificStatus
            sharedPref.edit().putBoolean(AppUnits.NOTIFICATION_STATUS_KEY, notificStatus).apply()
            if (notificStatus) {
                btn_wr_notification.setImageDrawable(getDrawable(R.drawable.ic_on_notification))
                Toast.makeText(this, "Notification Enabled", Toast.LENGTH_SHORT).show()
                alarm.setAlarm(
                    this,
                    sharedPref.getInt(AppUnits.NOTIFICATION_FREQUENCY_KEY, 30).toLong()
                )
            } else {
                btn_wr_notification.setImageDrawable(getDrawable(R.drawable.ic_off_notification))
                Toast.makeText(this, "Notification Disabled", Toast.LENGTH_SHORT).show()
                alarm.cancelAlarm(this)
            }
        }

        card_wr_50ml.setOnClickListener {
            selectedOption = 50
            card_wr_50ml.background = getDrawable(R.drawable.option_select_bg)
            card_wr_100ml.background = getDrawable(outValue.resourceId)
            card_wr_150ml.background = getDrawable(outValue.resourceId)
            card_wr_200ml.background = getDrawable(outValue.resourceId)
            card_wr_250ml.background = getDrawable(outValue.resourceId)
            card_wr_custom.background = getDrawable(outValue.resourceId)
        }

        card_wr_100ml.setOnClickListener {
            selectedOption = 100
            card_wr_50ml.background = getDrawable(outValue.resourceId)
            card_wr_100ml.background = getDrawable(R.drawable.option_select_bg)
            card_wr_150ml.background = getDrawable(outValue.resourceId)
            card_wr_200ml.background = getDrawable(outValue.resourceId)
            card_wr_250ml.background = getDrawable(outValue.resourceId)
            card_wr_custom.background = getDrawable(outValue.resourceId)
        }

        card_wr_150ml.setOnClickListener {
            selectedOption = 150
            card_wr_50ml.background = getDrawable(outValue.resourceId)
            card_wr_100ml.background = getDrawable(outValue.resourceId)
            card_wr_150ml.background =  getDrawable(R.drawable.option_select_bg)
            card_wr_200ml.background = getDrawable(outValue.resourceId)
            card_wr_250ml.background = getDrawable(outValue.resourceId)
            card_wr_custom.background = getDrawable(outValue.resourceId)
        }

        card_wr_150ml.setOnClickListener {
            selectedOption = 200
            card_wr_50ml.background = getDrawable(outValue.resourceId)
            card_wr_100ml.background = getDrawable(outValue.resourceId)
            card_wr_150ml.background = getDrawable(outValue.resourceId)
            card_wr_200ml.background = getDrawable(R.drawable.option_select_bg)
            card_wr_250ml.background = getDrawable(outValue.resourceId)
            card_wr_custom.background = getDrawable(outValue.resourceId)
        }
        card_wr_150ml.setOnClickListener {
            selectedOption = 250
            card_wr_50ml.background = getDrawable(outValue.resourceId)
            card_wr_100ml.background = getDrawable(outValue.resourceId)
            card_wr_150ml.background = getDrawable(outValue.resourceId)
            card_wr_200ml.background = getDrawable(outValue.resourceId)
            card_wr_250ml.background = getDrawable(R.drawable.option_select_bg)
            card_wr_custom.background = getDrawable(outValue.resourceId)
        }

        card_wr_custom.setOnClickListener {
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
                if (!TextUtils.isEmpty(inputText)) {
                    txt_wr_custom.text = "$inputText ml"
                    selectedOption = inputText.toInt()
                }
                alert.dismiss()
            }

            alert.show()

            card_wr_50ml.background = getDrawable(outValue.resourceId)
            card_wr_100ml.background = getDrawable(outValue.resourceId)
            card_wr_150ml.background = getDrawable(outValue.resourceId)
            card_wr_200ml.background = getDrawable(outValue.resourceId)
            card_wr_250ml.background = getDrawable(outValue.resourceId)
            card_wr_custom.background = getDrawable(R.color.white)
        }
    }

    fun updateValues() {
        totalIntake = sharedPref.getInt(AppUnits.TOTAL_INTAKE, 0)
        inTook = sqliteHelper.getIntook(dateNow)
        setWaterLevel(inTook, totalIntake)
    }

    private fun setWaterLevel(inTook: Int, totalIntake: Int) {

        txt_wr_Intook.text = "$inTook"
        txt_wr_Totalintake.text = "Goal of the day $totalIntake ml"

        val progress = ((inTook / totalIntake.toFloat()) * 100).toInt()
        wr_intake_progress.progress = progress
        if ((inTook * 100 / totalIntake) > 140) {
            Toast.makeText(this, "You achieved the goal", Toast.LENGTH_SHORT).show()
        }
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