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
import com.example.health_and_fitness.databinding.ActivityChildrensHeightBinding
import com.google.android.material.textfield.TextInputEditText

class Childrens_Height : AppCompatActivity() {
    private lateinit var chp_male: RadioButton
    private lateinit var btn_chp: Button
    private lateinit var chp_fheight: TextInputEditText
    private lateinit var chp_mheight: TextInputEditText
    private lateinit var binding: ActivityChildrensHeightBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_childrens_height)
        binding = ActivityChildrensHeightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Childrens Height Calculator"

        btn_chp = findViewById(R.id.btn_chp)
        chp_male = findViewById(R.id.chp_male)
        chp_fheight = findViewById(R.id.chp_fheight)
        chp_mheight = findViewById(R.id.chp_mheight)



        btn_chp.setOnClickListener {
            if (ValidateData_chp()) {
                calculatechp()
                showResult()
                chp_fheight.text!!.clear()
                chp_mheight.text!!.clear()
                chp_mheight.clearFocus()
            }

        }
    }

    private fun calculatechp(): Float {
        val fheight = chp_fheight.text.toString().toFloat()
        val mheight = chp_mheight.text.toString().toFloat()
        val father_height = fheight * 30.48
        val mother_height = mheight * 30.48
        if (chp_male.isChecked()) {
            var chp_male_result = ((father_height + mother_height + 13) / 2) * 0.0328
            return chp_male_result.toFloat()
        } else {
            var chp_female_result = ((father_height + mother_height - 13) / 2) * 0.0328
            return chp_female_result.toFloat()
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
        text_bmr1.text = String.format("Your Child's Predicted Height is : ")
        text_bmr2.text = String.format(calculatechp().toString() + " feet")

        dialog.show()
    }

    private fun ValidateData_chp(): Boolean {

        if (binding.chpFheight.text.toString() == "") {
            binding.chpFheightField.error = "Father Height is required"
            binding.chpFheight.requestFocus()
            return false
        } else {
            binding.chpFheightField.isErrorEnabled = false
        }
        if (binding.chpMheight.text.toString() == "") {
            binding.chpMheightField.error = "Mother Height is required"
            binding.chpMheight.requestFocus()
            return false
        } else {
            binding.chpMheightField.isErrorEnabled = false
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