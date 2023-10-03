package com.example.health_and_fitness.Activitys


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Step_Counter.Service.StepCounterService
import com.example.health_and_fitness.databinding.ActivityStepCounterBinding

class Step_Counter : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityStepCounterBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_step_counter)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_step_counter) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.step_counter_frag,
                R.id.step_status_frag,
                R.id.step_settings_frag
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bnvStepCounter.setupWithNavController(navController)

        startStepCounterService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askForNotificationPermission()
        }


    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askForNotificationPermission() {
        val notificationPermission = android.Manifest.permission.POST_NOTIFICATIONS
        val notificationPermissionStatus =
            ContextCompat.checkSelfPermission(this, notificationPermission)
        if (notificationPermissionStatus == PackageManager.PERMISSION_DENIED) {
            requestPermissionLauncher.launch(notificationPermission)
        }
    }

    private fun startStepCounterService() {
        val intent = Intent(this, StepCounterService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }


}