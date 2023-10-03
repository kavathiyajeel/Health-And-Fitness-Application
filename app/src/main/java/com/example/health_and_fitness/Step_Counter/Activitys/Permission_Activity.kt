package com.example.health_and_fitness.Step_Counter.Activitys


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.health_and_fitness.Activitys.Step_Counter
import com.example.health_and_fitness.R

class Permission_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_permission)
        supportActionBar!!.hide()
        if (shouldOpenOnboarding()) {
            openOnboardingActivity()
        } else {
            openMainActivity()
        }
        finish()
    }

    private fun shouldOpenOnboarding(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return false
        }
        val permission = Manifest.permission.ACTIVITY_RECOGNITION
        return !hasPermission(this, permission)
    }

    private fun openOnboardingActivity() {
        val intent = Intent(this, Onboarding_Activity::class.java)
        startActivity(intent)
    }

    private fun openMainActivity() {
        val intent = Intent(this, Step_Counter::class.java)
        startActivity(intent)
    }

    @Suppress("SameParameterValue")
    private fun hasPermission(context: Context, permission: String): Boolean {
        val status = ContextCompat.checkSelfPermission(context, permission)
        return status == PackageManager.PERMISSION_GRANTED
    }
}