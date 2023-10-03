package com.example.health_and_fitness.Health_Calculator

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.ActivityBloodSugerBinding
import com.google.android.material.textfield.TextInputEditText

class Blood_Suger : AppCompatActivity() {
    private lateinit var bsc_1: RadioButton
    private lateinit var btn_bsc: Button
    private lateinit var bsc_suger: TextInputEditText
    private lateinit var bsc_chart : TextView
    private lateinit var binding: ActivityBloodSugerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_blood_suger)
        binding = ActivityBloodSugerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Blood Suger Calculator"

        btn_bsc = findViewById(R.id.btn_bsc)
        bsc_suger = findViewById(R.id.bsc_suger)
        bsc_1 = findViewById(R.id.bsc_1)
        bsc_chart = findViewById(R.id.bsc_chart)



        bsc_chart.setOnClickListener {
            val intent = Intent(this,Blood_Suger_Chart::class.java)
            startActivity(intent)
        }

        btn_bsc.setOnClickListener {
            if(ValidateData_bs()) {
                calculatebsc()
                showResult()
                bsc_suger.text!!.clear()
                bsc_suger.clearFocus()
            }

        }
    }

    private fun calculatebsc() : Float {
        val suger_value = bsc_suger.text.toString().toFloat()
        if (bsc_1.isChecked()) {
            var bsc_1 = suger_value / 18
            return bsc_1
        } else {
            var bsc_2 = suger_value * 18
            return bsc_2
        }
    }

    private fun showResult() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.result)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var btn_close = dialog.findViewById<ImageView>(R.id.btn_close)
        var text_bmr1 = dialog.findViewById<TextView>(R.id.txt1)
        var text_bmr2 = dialog.findViewById<TextView>(R.id.txt2)

        btn_close.setOnClickListener {
            dialog.dismiss()
        }
        text_bmr1.text = String.format("Your Blood Suger Level is : ")
        text_bmr2.text = String.format(calculatebsc().toString() + " mm/ol/L")

        dialog.show()
    }

    private fun ValidateData_bs() : Boolean {

        if (binding.bscSuger.text.toString() == "") {
            binding.bpcSugerField.error = "Blood Suger is required"
            binding.bscSuger.requestFocus()
            return false
        } else {
            binding.bpcSugerField.isErrorEnabled = false
        }

        return true
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