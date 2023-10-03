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
import com.example.health_and_fitness.databinding.ActivityCholestrolRatioBinding
import com.google.android.material.textfield.TextInputEditText

class Cholestrol_Ratio : AppCompatActivity() {
    private lateinit var btn_cr: Button
    private lateinit var cr_hdl: TextInputEditText
    private lateinit var cr_ldl: TextInputEditText
    private lateinit var cr_triglyceride: TextInputEditText
    private lateinit var binding :ActivityCholestrolRatioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cholestrol_ratio)
        binding = ActivityCholestrolRatioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Cholestrol Ratio Calculator"


        btn_cr = findViewById(R.id.btn_cr)
        cr_hdl = findViewById(R.id.cr_hdl)
        cr_ldl = findViewById(R.id.cr_ldl)
        cr_triglyceride = findViewById(R.id.cr_triglyceride)


        btn_cr.setOnClickListener {
            if (ValidateData_cr()) {
                calculatecr()
                showResult()
                cr_hdl.text!!.clear()
                cr_ldl.text!!.clear()
                cr_triglyceride.text!!.clear()
                cr_triglyceride.clearFocus()
            }

        }
    }
    private fun calculatecr() :Float {
        val hdl = cr_hdl.text.toString().toFloat()
        val ldl = cr_ldl.text.toString().toFloat()
        val triglyceride = cr_triglyceride.text.toString().toFloat()
        var cr_result = hdl+ + ldl + (0.2 * triglyceride)
        return cr_result.toFloat()
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
        text_bmr1.text = String.format("Your Cholestrol Ratio is : ")
        text_bmr2.text = String.format(calculatecr().toString() + " mg/dl")

        dialog.show()
    }

    private fun ValidateData_cr() : Boolean {

        if (binding.crHdl.text.toString() == "") {
            binding.crHdlField.error = "HDL is required"
            binding.crHdl.requestFocus()
            return false
        } else {
            binding.crHdlField.isErrorEnabled = false
        }
        if (binding.crLdl.text.toString() == "") {
            binding.crLdlField.error = "LDL is required"
            binding.crLdl.requestFocus()
            return false
        } else {
            binding.crLdlField.isErrorEnabled = false
        }
        if (binding.crTriglyceride.text.toString() == "") {
            binding.crTriglycerideField.error = "Triglyceride is required"
            binding.crTriglyceride.requestFocus()
            return false
        } else {
            binding.crTriglycerideField.isErrorEnabled = false
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