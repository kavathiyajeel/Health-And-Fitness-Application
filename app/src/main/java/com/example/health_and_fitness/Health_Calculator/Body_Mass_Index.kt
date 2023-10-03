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
import android.widget.TextView
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.ActivityBodyMassIndexBinding
import com.google.android.material.textfield.TextInputEditText

class Body_Mass_Index : AppCompatActivity() {
    private lateinit var btn_bmi: Button
    private lateinit var bmi_height: TextInputEditText
    private lateinit var bmi_weight: TextInputEditText
    private lateinit var bmi_chart: TextView
    private lateinit var binding :ActivityBodyMassIndexBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_body_mass_index)
        binding = ActivityBodyMassIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Body Mass Calculator"


        btn_bmi = findViewById(R.id.btn_bmi)
        bmi_height = findViewById(R.id.bmi_height)
        bmi_weight = findViewById(R.id.bmi_weight)
        bmi_chart = findViewById(R.id.bmi_chart)


        bmi_chart.setOnClickListener {
           val intent = Intent(this,Body_Mass_Index_Chart::class.java)
            startActivity(intent)
        }

        btn_bmi.setOnClickListener {
            if (ValidateData_bmi()) {
                calculatebmi()
                showResult()
                bmi_height.text!!.clear()
                bmi_weight.text!!.clear()
                bmi_weight.clearFocus()
            }

        }
    }
    private fun calculatebmi(): Float {
        val weight = bmi_weight.text.toString().toFloat()
        val height = bmi_height.text.toString().toFloat()
        val heightInMeters = height * 0.3048
        val bmi_result = weight / (heightInMeters * heightInMeters)
        return bmi_result.toFloat()

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
        text_bmr1.text = String.format("Your Body Mass Index is : ")
        text_bmr2.text = String.format(calculatebmi().toString() )

        dialog.show()
    }

    private fun ValidateData_bmi() : Boolean {

        if (binding.bmiHeight.text.toString() == "") {
            binding.bmiHeightField.error = "Height is required"
            binding.bmiHeight.requestFocus()
            return false
        } else {
            binding.bmiHeightField.isErrorEnabled = false
        }
        if (binding.bmiWeight.text.toString() == "") {
            binding.bmiWeightField.error = "Weight is required"
            binding.bmiWeight.requestFocus()
            return false
        } else {
            binding.bmiWeightField.isErrorEnabled = false
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