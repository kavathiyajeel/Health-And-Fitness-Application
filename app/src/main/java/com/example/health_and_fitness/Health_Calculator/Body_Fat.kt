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
import com.example.health_and_fitness.databinding.ActivityBodyFatBinding
import com.google.android.material.textfield.TextInputEditText

class Body_Fat : AppCompatActivity() {
    private lateinit var bf_male: RadioButton
    private lateinit var btn_bf: Button
    private lateinit var bf_height: TextInputEditText
    private lateinit var bf_weight: TextInputEditText
    private lateinit var bf_chart: TextView
    private lateinit var binding: ActivityBodyFatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_body_fat)
        binding = ActivityBodyFatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Body Fat Calculator"


        btn_bf = findViewById(R.id.btn_bf)
        bf_male = findViewById(R.id.bf_male)
        bf_height = findViewById(R.id.bf_height)
        bf_weight = findViewById(R.id.bf_weight)
        bf_chart = findViewById(R.id.bf_chart)

        bf_chart.setOnClickListener {
            val intent = Intent(this,Body_Fat_Chart::class.java)
            startActivity(intent)
        }

        btn_bf.setOnClickListener {
            if (ValidateData_bf()) {
                calculatebf()
                showResult()
                bf_height.text!!.clear()
                bf_weight.text!!.clear()
                bf_weight.clearFocus()
            }

        }
    }

    private fun calculatebf(): Float {
        val weight = bf_weight.text.toString().toFloat()
        val height = bf_height.text.toString().toFloat()
        val heightInInches = height * 12
        if (bf_male.isChecked()) {
            var bf_male_result = (weight * 703) / (heightInInches * heightInInches)
            return bf_male_result
        } else {
            var bf_female_result = (weight * 697) / (heightInInches * heightInInches)
            return bf_female_result
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
        text_bmr1.text = String.format("Your Body Fat is : ")
        text_bmr2.text = String.format(calculatebf().toString())

        dialog.show()
    }

    private fun ValidateData_bf(): Boolean {


        if (binding.bfHeight.text.toString() == "") {
            binding.bfHeightField.error = "Height is required"
            binding.bfHeight.requestFocus()
            return false
        } else {
            binding.bfHeightField.isErrorEnabled = false
        }
        if (binding.bfWeight.text.toString() == "") {
            binding.bfWeightField.error = "Weight is required"
            binding.bfWeight.requestFocus()
            return false
        } else {
            binding.bfWeightField.isErrorEnabled = false
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