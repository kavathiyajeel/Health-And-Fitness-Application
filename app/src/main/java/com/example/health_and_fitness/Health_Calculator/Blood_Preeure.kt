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
import com.example.health_and_fitness.databinding.ActivityBloodPreeureBinding
import com.google.android.material.textfield.TextInputEditText

class Blood_Preeure : AppCompatActivity() {
    private lateinit var btn_bpc: Button
    private lateinit var bpc_sp: TextInputEditText
    private lateinit var bpc_dp: TextInputEditText
    private lateinit var bpc_chart : TextView
    private lateinit var binding: ActivityBloodPreeureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_blood_preeure)
        binding = ActivityBloodPreeureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Blood Preeure Calculator"

        btn_bpc = findViewById(R.id.btn_bpc)
        bpc_sp = findViewById(R.id.bpc_sp)
        bpc_dp = findViewById(R.id.bpc_dp)
        bpc_chart = findViewById(R.id.bpc_chart)



        bpc_chart.setOnClickListener {
            val intent = Intent(this,Blood_Preeure_Chart::class.java)
            startActivity(intent)
        }

        btn_bpc.setOnClickListener {
            if (ValidateData_bp()) {
                calculatebpc_sp()
                calculatebpc_dp()
                showResult()
                bpc_sp.text!!.clear()
                bpc_dp.text!!.clear()
                bpc_dp.clearFocus()
            }

        }
    }
    private fun calculatebpc_sp(): String {
        val sp = bpc_sp.text.toString().toFloat()

        val result_sp :String
        when {
            sp < 120  -> result_sp = "Normal"
            120 <= sp && sp <= 139 -> result_sp = "Elevated"
            140 <= sp && sp <= 159 -> result_sp = "Stage 1 hypertension"
            sp >= 160  -> result_sp = "Stage 2 hypertension"
            else -> result_sp = "Invalid input"
        }
        return result_sp


    }

    private fun calculatebpc_dp(): String {
        val dp = bpc_dp.text.toString().toFloat()

        val result_dp :String
        when {
            dp < 80  -> result_dp = "Normal"
            80 <= dp && dp <= 89 -> result_dp = "Elevated"
            90 <= dp && dp <= 99 -> result_dp = "Stage 1 hypertension"
            dp >= 100  -> result_dp = "Stage 2 hypertension"
            else -> result_dp = "Invalid input"
        }
        return result_dp
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
        text_bmr1.text = String.format("Blood Pressure is : " +calculatebpc_sp())
        text_bmr2.text = String.format("Diastolic Pressure is : " +calculatebpc_dp())

        dialog.show()
    }

    private fun ValidateData_bp() : Boolean {

        if (binding.bpcSp.text.toString() == "") {
            binding.bpcSpField.error = "Systolic Pressure is required"
            binding.bpcSp.requestFocus()
            return false
        } else {
            binding.bpcSpField.isErrorEnabled = false
        }
        if (binding.bpcDp.text.toString() == "") {
            binding.bpcDpField.error = "Diastolic Pressure is required"
            binding.bpcDp.requestFocus()
            return false
        } else {
            binding.bpcDpField.isErrorEnabled = false
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