package com.example.health_and_fitness.Login_And_SignUp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.health_and_fitness.MainActivity
import com.example.health_and_fitness.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login_And_SignUp : AppCompatActivity() {
    companion object {
        const val RC_SIGNIN = 25
    }

    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_login_and_sign_up)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        checkConnection()
        supportActionBar!!.hide()

        val btn_mobileno = findViewById(R.id.btn_mobileno) as Button
        btn_mobileno.setOnClickListener {
            val intent = Intent(this, Mobile_Number::class.java)
            startActivity(intent)
        }
        val btn_google = findViewById(R.id.btn_google) as Button
        btn_google.setOnClickListener {
            signIn()
        }

        //google login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    //google login
    private fun signIn() {
        val signIntent = googleSignInClient.signInIntent
        startActivityForResult(signIntent, RC_SIGNIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNIN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                doAuthentication(account!!.idToken)
            } catch (e: Exception) {

            }
        }
    }

    private fun doAuthentication(idToken: String?) {
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credentials).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Login Sucessfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
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
}