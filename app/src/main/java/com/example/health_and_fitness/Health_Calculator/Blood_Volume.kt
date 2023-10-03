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
import com.example.health_and_fitness.databinding.ActivityBloodVolumeBinding
import com.google.android.material.textfield.TextInputEditText

class Blood_Volume : AppCompatActivity() {
    private lateinit var bvc_male: RadioButton
    private lateinit var btn_bvc: Button
    private lateinit var bvc_height: TextInputEditText
    private lateinit var bvc_weight: TextInputEditText
    private lateinit var binding: ActivityBloodVolumeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_blood_volume)
        binding = ActivityBloodVolumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Blood Volume Calculator"

        btn_bvc = findViewById(R.id.btn_bvc)
        bvc_male = findViewById(R.id.bvc_male)
        bvc_height = findViewById(R.id.bvc_height)
        bvc_weight = findViewById(R.id.bvc_weight)



        btn_bvc.setOnClickListener {
            if (ValidateData_bv()) {
                calculatebvc()
                showResult()
                bvc_height.text!!.clear()
                bvc_weight.text!!.clear()
                bvc_weight.clearFocus()
            }

        }
    }

    private fun calculatebvc():Float {
        val weight = bvc_weight.text.toString().toFloat()
        val height = bvc_height.text.toString().toFloat()
        if (bvc_male.isChecked()) {
            val bvc_male_result = (0.3669 * (height / 3.28084) * (height / 3.28084) * (height / 3.28084) + (0.03219 * weight) + 0.6041)
            return bvc_male_result.toFloat()
        } else {
            val bvc_female_result = (0.3561  * (height / 3.28084) * (height / 3.28084) * (height / 3.28084) + (0.03308  * weight) + 0.1833)
            return bvc_female_result.toFloat()
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
        text_bmr1.text = String.format("Your Blood Volume is : ")
        text_bmr2.text = String.format(calculatebvc().toString() + " liter")

        dialog.show()
    }

    private fun ValidateData_bv() : Boolean {

        if (binding.bvcHeight.text.toString() == "") {
            binding.bvcHeightField.error = "Height is required"
            binding.bvcHeight.requestFocus()
            return false
        } else {
            binding.bvcHeightField.isErrorEnabled = false
        }
        if (binding.bvcWeight.text.toString() == "") {
            binding.bvcWeightField.error = "Weight is required"
            binding.bvcWeight.requestFocus()
            return false
        } else {
            binding.bvcWeightField.isErrorEnabled = false
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