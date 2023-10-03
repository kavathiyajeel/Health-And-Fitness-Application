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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.ActivityCalorieBurnBinding
import com.google.android.material.textfield.TextInputEditText

class Calorie_Burn : AppCompatActivity() {
    private lateinit var btn_cb: Button
    private lateinit var cb_distance: TextInputEditText
    private lateinit var cb_weight: TextInputEditText
    private lateinit var cb_met: AutoCompleteTextView
    private lateinit var cb_chart: TextView
    private lateinit var binding:ActivityCalorieBurnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_calorie_burn)
        binding = ActivityCalorieBurnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Calorie Burn Calculator"


        btn_cb = findViewById(R.id.btn_cb)
        cb_distance = findViewById(R.id.cb_distance)
        cb_weight = findViewById(R.id.cb_weight)
        cb_met = findViewById(R.id.cb_met)
        cb_chart = findViewById(R.id.cb_chart)

        val listMet = ArrayList<String>()
        listMet.add("running")
        listMet.add("cycling")
        listMet.add("walking")

        val metAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listMet)
        cb_met.setAdapter(metAdapter)



        btn_cb.setOnClickListener {
            if (ValidateData_cb()) {
                calculatecb()
                showResult()
                cb_weight.text!!.clear()
                cb_distance.text!!.clear()
                cb_met.text!!.clear()
                cb_distance.clearFocus()
            }

        }
        cb_chart.setOnClickListener {
           val intent = Intent(this,Calories_Burn_Chart::class.java)
            startActivity(intent)
        }
    }

    private fun calculatecb(): Float {
        val weight = cb_weight.text.toString().toFloat()
        val distance = cb_distance.text.toString().toFloat()
        val activity = cb_met.text.toString()
        val met = when (activity) {
            "running" -> 8.0
            "cycling" -> 6.0
            "walking" -> 3.0
            else -> 1.0
        }
        return ((weight * distance * met) / 200).toFloat()
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
        text_bmr1.text = String.format("Your Calories Burned is : ")
        text_bmr2.text = String.format(calculatecb().toString())

        dialog.show()
    }

    private fun ValidateData_cb(): Boolean {

        if (binding.cbWeight.text.toString() == "") {
            binding.cbWeightField.error = "Weight is required"
            binding.cbWeight.requestFocus()
            return false
        } else {
            binding.cbWeightField.isErrorEnabled = false
        }
        if (binding.cbDistance.text.toString() == "") {
            binding.cbDistanceField.error = "Duration Time is required"
            binding.cbDistance.requestFocus()
            return false
        } else {
            binding.cbDistanceField.isErrorEnabled = false
        }
        if (binding.cbMet.text.toString() == "") {
            binding.cbMetField.error = "Select the Met is required"
            binding.cbMet.requestFocus()
            return false
        } else {
            binding.cbMetField.isErrorEnabled = false
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