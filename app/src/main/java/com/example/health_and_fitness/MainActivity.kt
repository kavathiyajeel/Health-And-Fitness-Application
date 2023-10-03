package com.example.health_and_fitness

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.health_and_fitness.Activitys.Health_Calculator
import com.example.health_and_fitness.Activitys.Medicine_Remainder
import com.example.health_and_fitness.Activitys.Water_Remainder
import com.example.health_and_fitness.Login_And_SignUp.Login_And_SignUp
import com.example.health_and_fitness.Step_Counter.Activitys.Permission_Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerlayout_main: DrawerLayout
    private lateinit var nav_view: NavigationView
    private lateinit var user_name: TextView
    private lateinit var user_email: TextView
    private lateinit var user_image: ImageView
    private lateinit var card_health_calculator: CardView
    private lateinit var card_medicine_remainder: CardView
    private lateinit var card_exercise_steps: CardView
    private lateinit var card_step_counder: CardView
    private lateinit var card_water_remainder: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)

        // actionbar color
        val actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        actionBar?.setBackgroundDrawable(colorDrawable)


        drawerlayout_main = findViewById(R.id.drawerlayout_main)
        nav_view = findViewById(R.id.nav_view)

        //navigation bar
        toggle = ActionBarDrawerToggle(this, drawerlayout_main, R.string.open, R.string.close)
        drawerlayout_main.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show()
                R.id.appinfo -> Toast.makeText(applicationContext, "App Info", Toast.LENGTH_SHORT)
                    .show()

            }
            true
        }


        card_health_calculator = findViewById(R.id.card_health_calculator)
        card_medicine_remainder = findViewById(R.id.card_medicine_remainder)
        //card_exercise_steps = findViewById(R.id.card_exercise_steps)
        card_step_counder = findViewById(R.id.card_step_counder)
        card_water_remainder = findViewById(R.id.card_water_remainder)

        card_health_calculator.setOnClickListener {
            val intent = Intent(this, Health_Calculator::class.java)
            startActivity(intent)
        }
        /*card_exercise_steps.setOnClickListener {
            val intent = Intent(this, Exercise_steps::class.java)
            startActivity(intent)
        }*/
        card_medicine_remainder.setOnClickListener {
            val intent = Intent(this, Medicine_Remainder::class.java)
            startActivity(intent)
        }
        card_step_counder.setOnClickListener {
            val intent = Intent(this, Permission_Activity::class.java)
            startActivity(intent)
        }
        card_water_remainder.setOnClickListener {
            val intent = Intent(this, Water_Remainder::class.java)
            startActivity(intent)
        }


        checkConnection()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    //internet connection
    private fun checkConnection() {
        val manager =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (null != networkInfo) {

        } else {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.no_internet_connection)
            dialog.setCanceledOnTouchOutside(false)

            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btn_retry = dialog.findViewById<TextView>(R.id.btn_retry)
            btn_retry.setOnClickListener {
                recreate()
            }
            dialog.show()
        }
    }

    // Side Navigation bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser

        nav_view.setNavigationItemSelectedListener(this)
        val view = nav_view.getHeaderView(0)
        user_name = view.findViewById(R.id.user_name)
        user_email = view.findViewById(R.id.user_email)
        user_image = view.findViewById(R.id.user_image)

        if (currentUser != null) {
            // User is logged in
            val providerData = currentUser.providerData
            for (userInfo in providerData) {
                when (userInfo.providerId) {
                    GoogleAuthProvider.PROVIDER_ID -> {
                        user_name.text = currentUser.displayName
                        user_email.text = currentUser.email
                        val photoUrl = currentUser.photoUrl
                        Glide.with(this).load(photoUrl).into(user_image)
                    }
                    PhoneAuthProvider.PROVIDER_ID -> {
                        user_name.text = currentUser.phoneNumber
                        user_image.setImageResource(R.drawable.ic_user)
                    }
                }
            }
        } else {
            val intent = Intent(this, Login_And_SignUp::class.java)
            startActivity(intent)
            finish()
        }


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                FirebaseAuth.getInstance().signOut()
                finish()
            }
        }
        return true

    }
}