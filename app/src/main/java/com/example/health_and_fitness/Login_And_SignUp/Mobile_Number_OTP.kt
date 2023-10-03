package com.example.health_and_fitness.Login_And_SignUp

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.health_and_fitness.MainActivity
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.ActivityMobileNumberOtpBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class Mobile_Number_OTP : AppCompatActivity() {
    private lateinit var btn_verify: Button
    private lateinit var resend_otp: TextView
    private lateinit var otp_timer: TextView
    private lateinit var mn_otp_number: TextView
    private lateinit var otp_pinview: EditText
    private lateinit var back_mobile_no_otp: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var mobile_no_otp_loading: ProgressBar
    private lateinit var binding: ActivityMobileNumberOtpBinding
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var pnumber: String = ""
    private lateinit var otpResendTimer: CountDownTimer

    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileNumberOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        supportActionBar!!.hide()

        btn_verify = findViewById(R.id.btn_verify)
        resend_otp = findViewById(R.id.resend_otp)
        otp_timer = findViewById(R.id.otp_timer)
        mn_otp_number = findViewById(R.id.mn_otp_number)
        mobile_no_otp_loading = findViewById(R.id.mobile_no_otp_loading)
        otp_pinview = findViewById(R.id.otp_pinview)
        back_mobile_no_otp = findViewById(R.id.back_mobile_no_otp)

        back_mobile_no_otp.setOnClickListener {
            val intent = Intent(this, Mobile_Number::class.java)
            startActivity(intent)
        }
        //resend_otp.isEnabled = false
        otpResendTimer = object : CountDownTimer(60000,1){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000
                otp_timer.text = "$seconds Sec"
            }

            override fun onFinish() {
                otp_timer.visibility = View.INVISIBLE
//                resend_otp.isEnabled = true
                otp_pinview.text.clear()
                resend_otp.text = getString(R.string.resend_otp)

            }
        }.start()


        /// auto otp
        smsBroadcastReceiver = SmsBroadcastReceiver()
        //val filter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        //registerReceiver(smsBroadcastReceiver, filter)

        startSmsRetriever()



        auth = FirebaseAuth.getInstance()
        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        storedVerificationId = intent.getStringExtra("storedVerificationId")!!
        resendToken = intent.getParcelableExtra("resendToken")!!
        pnumber = intent.getStringExtra("pnumber")!!

        resend_otp.setOnClickListener {
            resendVerificationCode(pnumber)

        }

        mn_otp_number.text = String.format(pnumber)

        btn_verify.setOnClickListener {
            otp()

        }
    }
    private fun otp() {
        mobile_no_otp_loading.visibility = View.VISIBLE
        btn_verify.visibility = View.INVISIBLE
        val otp = otp_pinview.text?.trim().toString()
        if (otp_pinview.text.toString().length == 6) {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                storedVerificationId, otp
            )
            signInWithPhoneAuthCredential(credential)
        } else {
            mobile_no_otp_loading.visibility = View.GONE
            btn_verify.visibility = View.VISIBLE
            Toast.makeText(this, "Enter 6 Digit Code", Toast.LENGTH_SHORT).show()

        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                mobile_no_otp_loading.visibility = View.GONE
                btn_verify.visibility = View.VISIBLE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Sucessfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "The Vetification code is Invaild", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun resendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            /*startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()*/
            signInWithPhoneAuthCredential(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            if(e is FirebaseAuthInvalidCredentialsException){
                Log.w(ContentValues.TAG,"Invaild phone number.    ")
            }else if (e is FirebaseTooManyRequestsException){
                Log.w(ContentValues.TAG,"Quota exceeded.")
            }
        }
        override fun onCodeSent(
            newverificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = newverificationId
            resendToken = token
        }
    }




    private fun startSmsRetriever() {
        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            // Listening for SMS messages successfully started
        }
        task.addOnFailureListener {
            // Error occurred while starting SMS retrieval
        }
    }

    private fun registerSmsBroadcastReceiver() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    private fun extractOtpFromMessage(message: String): String {
        val pattern = Pattern.compile("\\d{6}")
        val matcher = pattern.matcher(message)
        return if (matcher.find()) {
            matcher.group(0)
        } else {
            ""
        }
    }

    private fun fillInOtp(otp: String) {
        otp_pinview.setText(otp)
    }

    inner class SmsBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
                val extras = intent.extras
                val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        val message = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                        // Extract the OTP code from the message
                        val otp = extractOtpFromMessage(message)
                        // Fill in the OTP code in the EditText
                        fillInOtp(otp)
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // Handle the timeout
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsBroadcastReceiver)
    }
}