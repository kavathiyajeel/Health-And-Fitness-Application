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
import com.example.health_and_fitness.databinding.ActivityBloodAlchoolContentBinding
import com.google.android.material.textfield.TextInputEditText

class Blood_Alchool_Content : AppCompatActivity() {

    private lateinit var bac_male: RadioButton
    private lateinit var btn_bac: Button
    private lateinit var bac_drink: TextInputEditText
    private lateinit var bac_time: TextInputEditText
    private lateinit var bac_weight: TextInputEditText
    private lateinit var bac_chart: TextView
    private lateinit var binding: ActivityBloodAlchoolContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_blood_alchool_content)

        binding = ActivityBloodAlchoolContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /// Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Blood Alchool Calculator"

        btn_bac = findViewById(R.id.btn_bac)
        bac_male = findViewById(R.id.bac_male)
        bac_drink = findViewById(R.id.bac_drink)
        bac_time = findViewById(R.id.bac_time)
        bac_weight = findViewById(R.id.bac_weight)
        bac_chart = findViewById(R.id.bac_chart)


        btn_bac.setOnClickListener {
            if (ValidateData_bac()) {
                calculatebac()
                showResult()
                bac_drink.text!!.clear()
                bac_time.text!!.clear()
                bac_weight.text!!.clear()
                bac_weight.clearFocus()
            }
        }

        bac_chart.setOnClickListener {
            val intent =Intent(this,Blood_Alchool_Content_Chart::class.java)
            startActivity(intent)
        }
    }
    private fun calculatebac(): Float {
        val drink = bac_drink.text.toString().toFloat()
        val weight = bac_weight.text.toString().toFloat()
        val time = bac_time.text.toString().toFloat()
        if (bac_male.isChecked()) {
            var bac_male_result = (drink * (0.789 * 1000) / (weight * 1000) * 0.68) - (0.015 * time)
            return bac_male_result.toFloat()
        } else {
            var bac_female_result =
                (drink * (0.789 * 1000) / (weight * 1000) * 0.55) - (0.015 * time)
            return bac_female_result.toFloat()
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
        val btn_close = dialog.findViewById<ImageView>(R.id.btn_close)
        val text_bmr1 = dialog.findViewById<TextView>(R.id.txt1)
        val text_bmr2 = dialog.findViewById<TextView>(R.id.txt2)

        btn_close.setOnClickListener {
            dialog.dismiss()
        }
        text_bmr1.text = String.format("Your Blood Alcohol Content level is :")
        text_bmr2.text = String.format(calculatebac().toString())

        dialog.show()
    }

    private fun ValidateData_bac(): Boolean {

        if (binding.bacDrink.text.toString() == "") {
            binding.bacDrinkField.error = "Drink Volume is required"
            binding.bacDrink.requestFocus()
            return false
        } else {
            binding.bacDrinkField.isErrorEnabled = false
        }
        if (binding.bacTime.text.toString() == "") {
            binding.bacTimeField.error = "Drink Time is required"
            binding.bacTime.requestFocus()
            return false
        } else {
            binding.bacTimeField.isErrorEnabled = false
        }
        if (binding.bacWeight.text.toString() == "") {
            binding.bacWeightField.error = "Weight is required"
            binding.bacWeight.requestFocus()
            return false
        } else {
            binding.bacWeightField.isErrorEnabled = false
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