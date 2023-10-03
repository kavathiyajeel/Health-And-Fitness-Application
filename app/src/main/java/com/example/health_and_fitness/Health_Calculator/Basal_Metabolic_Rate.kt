package com.example.health_and_fitness.Health_Calculator

import android.app.Dialog
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
import com.example.health_and_fitness.databinding.ActivityBasalMetabolicRateBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Basal_Metabolic_Rate : AppCompatActivity() {

    private lateinit var bmrMale: RadioButton
    private lateinit var btnBmr: Button
    private lateinit var bmrAge: TextInputEditText
    private lateinit var bmrHeight: TextInputEditText
    private lateinit var bmrWeight: TextInputEditText
    private lateinit var bmrWeightField: TextInputLayout
    private lateinit var binding: ActivityBasalMetabolicRateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_basal_metabolic_rate)
        binding = ActivityBasalMetabolicRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Basal Metabolic Rate Calculator"

        btnBmr = findViewById(R.id.btn_bmr)
        bmrMale = findViewById(R.id.bmr_male)
        bmrAge = findViewById(R.id.bmr_age)
        bmrHeight = findViewById(R.id.bmr_height)
        bmrWeight = findViewById(R.id.bmr_weight)
        bmrWeightField = findViewById(R.id.bmr_weight_field)

        btnBmr.setOnClickListener {
            if (validateDataBmr()) {
                val bmrResult = calculateBmr()
                showResult(bmrResult)
                bmrAge.text!!.clear()
                bmrWeight.text!!.clear()
                bmrHeight.text!!.clear()
                bmrWeight.clearFocus()
            }
        }
    }

    private fun calculateBmr(): Float {
        val age = bmrAge.text.toString().toFloat()
        val weight = bmrWeight.text.toString().toFloat()
        val height = bmrHeight.text.toString().toFloat()

        return if (bmrMale.isChecked) {
            (66 + (13.7 * weight) + (5 * height * 30.48) - (6.8 * age)).toFloat()
        } else {
            (655 + (9.6 * weight) + (1.8 * height * 30.48) - (4.7 * age)).toFloat()
        }
    }

    private fun showResult(bmrResult: Float) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.result)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnClose = dialog.findViewById<ImageView>(R.id.btn_close)
        val textBmr1 = dialog.findViewById<TextView>(R.id.txt1)
        val textBmr2 = dialog.findViewById<TextView>(R.id.txt2)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        textBmr1.text = String.format("Your Basal Metabolic Rate is:")
        textBmr2.text = String.format("%.2f calories/day", bmrResult)

        dialog.show()
    }

    private fun validateDataBmr(): Boolean {
        if (binding.bmrAge.text.toString().isEmpty()) {
            binding.bmrAgeField.error = "Age is required"
            binding.bmrAge.requestFocus()
            return false
        } else {
            binding.bmrAgeField.isErrorEnabled = false
        }

        if (binding.bmrHeight.text.toString().isEmpty()) {
            binding.bmrHeightField.error = "Height is required"
            binding.bmrHeight.requestFocus()
            return false
        } else {
            binding.bmrHeightField.isErrorEnabled = false
        }

        if (binding.bmrWeight.text.toString().isEmpty()) {
            binding.bmrWeightField.error = "Weight is required"
            binding.bmrWeight.requestFocus()
            return false
        } else {
            binding.bmrWeightField.isErrorEnabled = false
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