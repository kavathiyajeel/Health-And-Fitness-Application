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
import com.example.health_and_fitness.databinding.ActivityBodyAdiposityIndexBinding
import com.google.android.material.textfield.TextInputEditText

class Body_Adiposity_Index : AppCompatActivity() {
    private lateinit var btn_bai: Button
    private lateinit var bai_height: TextInputEditText
    private lateinit var bai_hipc: TextInputEditText
    private lateinit var bai_chart: TextView
    private lateinit var binding: ActivityBodyAdiposityIndexBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_body_adiposity_index)
        binding = ActivityBodyAdiposityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Body Adiposity Index calculator"

        btn_bai = findViewById(R.id.btn_bai)
        bai_height =findViewById(R.id.bai_height)
        bai_hipc = findViewById(R.id.bai_hipc)
        bai_chart = findViewById(R.id.bai_chart)


        bai_chart.setOnClickListener {
            val intent = Intent(this,Body_Adiposity_Index_Chart::class.java)
            startActivity(intent)
        }

        btn_bai.setOnClickListener {
            if (ValidateData_bai()) {
                calculatebai()
                showResult()
                bai_hipc.text!!.clear()
                bai_height.text!!.clear()
                bai_height.clearFocus()
            }

        }
    }
    private fun calculatebai() : Float {
        val hipCircumference = bai_hipc.text.toString().toFloat()
        val height = bai_height.text.toString().toFloat()
        val heightInMeters = height * 0.3048
        val bai_result =
            (hipCircumference / (heightInMeters * heightInMeters)) - 18
        return bai_result.toFloat()

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
        text_bmr1.text = String.format("Your Body Adiposity Index is : ")
        text_bmr2.text = String.format(calculatebai().toString())

        dialog.show()
    }

    private fun ValidateData_bai() : Boolean {

        if (binding.baiHipc.text.toString() == "") {
            binding.baiHipcField.error = "Hip Circumference is required"
            binding.baiHipc.requestFocus()
            return false
        } else {
            binding.baiHipcField.isErrorEnabled = false
        }
        if (binding.baiHeight.text.toString() == "") {
            binding.baiHeightField.error = "Height is required"
            binding.baiHeight.requestFocus()
            return false
        } else {
            binding.baiHeightField.isErrorEnabled = false
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