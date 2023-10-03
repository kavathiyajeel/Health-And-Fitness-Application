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
import android.widget.TextView
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.ActivityBodySurfaceAreaBinding
import com.google.android.material.textfield.TextInputEditText

class Body_Surface_Area : AppCompatActivity() {
    private lateinit var btn_bsa: Button
    private lateinit var bsa_height: TextInputEditText
    private lateinit var bsa_weight: TextInputEditText
    private lateinit var binding: ActivityBodySurfaceAreaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_body_surface_area)
        binding = ActivityBodySurfaceAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Body Surface Area Calculator"

        btn_bsa = findViewById(R.id.btn_bsa)
        bsa_height = findViewById(R.id.bsa_height)
        bsa_weight = findViewById(R.id.bsa_weight)


        btn_bsa.setOnClickListener {
            if (ValidateData_bsa()) {
                calculatebsa()
                showResult()
                bsa_height.text!!.clear()
                bsa_weight.text!!.clear()
                bsa_weight.clearFocus()
            }

        }
    }

    private fun calculatebsa(): Float {
        val weight = bsa_weight.text.toString().toFloat()
        val height = bsa_height.text.toString().toFloat()
        val heightInMeters = height * 0.3048
        var bsa_result =
            0.20247 * Math.pow(heightInMeters, 0.725) * Math.pow(weight.toDouble(), 0.425)
        return bsa_result.toFloat()
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
        text_bmr1.text = String.format("Your Body Surface Area is : ")
        text_bmr2.text = String.format(calculatebsa().toString() + " m²")

        dialog.show()
    }

    private fun ValidateData_bsa(): Boolean {

        if (binding.bsaHeight.text.toString() == "") {
            binding.bsaHeightField.error = "Height is required"
            binding.bsaHeight.requestFocus()
            return false
        } else {
            binding.bsaHeightField.isErrorEnabled = false
        }
        if (binding.bsaWeight.text.toString() == "") {
            binding.bsaWeightField.error = "Weight is required"
            binding.bsaWeight.requestFocus()
            return false
        } else {
            binding.bsaWeightField.isErrorEnabled = false
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