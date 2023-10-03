package com.example.health_and_fitness.Activitys

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.cardview.widget.CardView
import com.example.health_and_fitness.Health_Calculator.Basal_Metabolic_Rate
import com.example.health_and_fitness.Health_Calculator.Blood_Alchool_Content
import com.example.health_and_fitness.Health_Calculator.Blood_Preeure
import com.example.health_and_fitness.Health_Calculator.Blood_Suger
import com.example.health_and_fitness.Health_Calculator.Blood_Volume
import com.example.health_and_fitness.Health_Calculator.Body_Adiposity_Index
import com.example.health_and_fitness.Health_Calculator.Body_Fat
import com.example.health_and_fitness.Health_Calculator.Body_Frame_Size
import com.example.health_and_fitness.Health_Calculator.Body_Mass_Index
import com.example.health_and_fitness.Health_Calculator.Body_Surface_Area
import com.example.health_and_fitness.Health_Calculator.Bruce_Trade_Mill
import com.example.health_and_fitness.Health_Calculator.Calorie_Burn
import com.example.health_and_fitness.Health_Calculator.Childrens_Height
import com.example.health_and_fitness.Health_Calculator.Cholestrol_Ratio
import com.example.health_and_fitness.R

class Health_Calculator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_calculator)
        /// Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Health Calculator"


        val basal_metabolic_rate = findViewById<CardView>(R.id.basal_metabolic_rate)
        val blood_alcohol_content = findViewById<CardView>(R.id.blood_alcohol_content)
        val blood_pressure = findViewById<CardView>(R.id.blood_pressure)
        val blood_suger = findViewById<CardView>(R.id.blood_suger)
        val blood_volume = findViewById<CardView>(R.id.blood_volume)
        val body_adiposity_index = findViewById<CardView>(R.id.body_adiposity_index)
        val body_fat = findViewById<CardView>(R.id.body_fat)
        val body_frame_size = findViewById<CardView>(R.id.body_frame_size)
        val body_mass_index = findViewById<CardView>(R.id.body_mass_index)
        val body_surface_area = findViewById<CardView>(R.id.body_surface_area)
        val bruce_trade_mill = findViewById<CardView>(R.id.bruce_trade_mill)
        val calorie_burn = findViewById<CardView>(R.id.calorie_burn)
        val children_height_predictor = findViewById<CardView>(R.id.children_height_predictor)
        val cholestrol_ratio = findViewById<CardView>(R.id.cholestrol_ratio)


        basal_metabolic_rate.setOnClickListener {
            val intent = Intent(this, Basal_Metabolic_Rate::class.java)
            startActivity(intent)
        }
        blood_alcohol_content.setOnClickListener {
            val intent = Intent(this, Blood_Alchool_Content::class.java)
            startActivity(intent)
        }
        blood_pressure.setOnClickListener {
            val intent = Intent(this, Blood_Preeure::class.java)
            startActivity(intent)
        }
        blood_suger.setOnClickListener {
            val intent = Intent(this, Blood_Suger::class.java)
            startActivity(intent)
        }
        blood_volume.setOnClickListener {
            val intent = Intent(this, Blood_Volume::class.java)
            startActivity(intent)
        }
        body_adiposity_index.setOnClickListener {
            val intent = Intent(this, Body_Adiposity_Index::class.java)
            startActivity(intent)
        }
        body_fat.setOnClickListener {
            val intent = Intent(this, Body_Fat::class.java)
            startActivity(intent)
        }
        body_frame_size.setOnClickListener {
            val intent = Intent(this, Body_Frame_Size::class.java)
            startActivity(intent)
        }
        body_mass_index.setOnClickListener {
            val intent = Intent(this, Body_Mass_Index::class.java)
            startActivity(intent)
        }
        body_surface_area.setOnClickListener {
            val intent = Intent(this, Body_Surface_Area::class.java)
            startActivity(intent)
        }
        bruce_trade_mill.setOnClickListener {
            val intent = Intent(this, Bruce_Trade_Mill::class.java)
            startActivity(intent)
        }
        calorie_burn.setOnClickListener {
            val intent = Intent(this, Calorie_Burn::class.java)
            startActivity(intent)
        }
        children_height_predictor.setOnClickListener {
            val intent = Intent(this, Childrens_Height::class.java)
            startActivity(intent)
        }
        cholestrol_ratio.setOnClickListener {
            val intent = Intent(this, Cholestrol_Ratio::class.java)
            startActivity(intent)
        }

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