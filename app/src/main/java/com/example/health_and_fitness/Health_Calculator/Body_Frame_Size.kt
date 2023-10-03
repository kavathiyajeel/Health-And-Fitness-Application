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
import com.example.health_and_fitness.databinding.ActivityBodyFrameSizeBinding
import com.google.android.material.textfield.TextInputEditText

class Body_Frame_Size : AppCompatActivity() {
    private lateinit var btn_bfs: Button
    private lateinit var bfs_height: TextInputEditText
    private lateinit var bfs_wrist: TextInputEditText
    private lateinit var binding : ActivityBodyFrameSizeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_body_frame_size)
        binding = ActivityBodyFrameSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Body Frame Size Calculator"


        btn_bfs = findViewById(R.id.btn_bfs)
        bfs_wrist = findViewById(R.id.bfs_wrist)
        bfs_height = findViewById(R.id.bfs_height)


        btn_bfs.setOnClickListener {
            if (ValidateData_bfs()) {
                calculatebfs()
                showResult()
                bfs_wrist.text!!.clear()
                bfs_height.text!!.clear()
                bfs_height.clearFocus()
            }

        }
    }

    private fun calculatebfs() : String {
        val wrist = bfs_wrist.text.toString().toFloat()
        val height = bfs_height.text.toString().toFloat()
        val heightInInches = height * 12
        val result_bfs :String
        when {
            wrist / heightInInches < 6.5 -> result_bfs = "Small"
            wrist / heightInInches in 6.5..7.5 -> result_bfs = "Medium"
            else -> result_bfs = "Large"
        }
        return result_bfs
    }

    private fun showResult() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.result_1)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var btn_close = dialog.findViewById<ImageView>(R.id.btn_close)
        var text_bmr1 = dialog.findViewById<TextView>(R.id.txt1)

        btn_close.setOnClickListener {
            dialog.dismiss()
        }
        text_bmr1.text = String.format("Blood Frame Size is : " +calculatebfs())

        dialog.show()
    }

    private fun ValidateData_bfs() : Boolean {

        if (binding.bfsWrist.text.toString() == "") {
            binding.bfsWristField.error = "Wrist Size is required"
            binding.bfsWrist.requestFocus()
            return false
        } else {
            binding.bfsWristField.isErrorEnabled = false
        }
        if (binding.bfsHeight.text.toString() == "") {
            binding.bfsHeightField.error = "Height is required"
            binding.bfsHeight.requestFocus()
            return false
        } else {
            binding.bfsHeightField.isErrorEnabled = false
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