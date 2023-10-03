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
import com.example.health_and_fitness.databinding.ActivityBruceTradeMillBinding
import com.google.android.material.textfield.TextInputEditText

class Bruce_Trade_Mill : AppCompatActivity() {
    private lateinit var btm_male: RadioButton
    private lateinit var btn_btm: Button
    private lateinit var btm_time: TextInputEditText
    private lateinit var binding: ActivityBruceTradeMillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_bruce_trade_mill)
        binding = ActivityBruceTradeMillBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Bruce Trade Mill Calculator"


        btn_btm = findViewById(R.id.btn_btm)
        btm_male = findViewById(R.id.btm_male)
        btm_time = findViewById(R.id.btm_time)

        btn_btm.setOnClickListener {
            if (ValidateData_btm()) {
                calculatebtm()
                showResult()
                btm_time.text!!.clear()
                btm_time.clearFocus()
            }

        }
    }

    private fun calculatebtm(): Float {
        val time = btm_time.text.toString().toFloat()

        if (btm_male.isChecked()) {
            var btm_male_result =
                14.8 - (1.379 * time) + (0.451 * time * time) - (0.012 * time * time * time)
            return btm_male_result.toFloat()
        } else {
            var btm_female_result = (4.38 * time) - 3.9
            return btm_female_result.toFloat()
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
        text_bmr1.text = String.format("Your Bruce Trade Mill is : ")
        text_bmr2.text = String.format(calculatebtm().toString() + " ml/kg/min")

        dialog.show()
    }

    private fun ValidateData_btm(): Boolean {

        if (binding.btmTime.text.toString() == "") {
            binding.btmTimeField.error = "Time is required"
            binding.btmTime.requestFocus()
            return false
        } else {
            binding.btmTimeField.isErrorEnabled = false
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