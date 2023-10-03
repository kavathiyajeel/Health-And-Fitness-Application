package com.example.health_and_fitness.Login_And_SignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.health_and_fitness.R
import com.example.health_and_fitness.databinding.ActivityMobileNumberBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class Mobile_Number : AppCompatActivity() {

    private lateinit var country_code : CountryCodePicker
    private lateinit var phone_number: TextInputEditText
    private lateinit var btn_generate_otp: Button
    private lateinit var mobile_no_loading : ProgressBar
    private lateinit var back_mobile_no: ImageView
    var number: String = ""
    var ccode: String = ""
    var pnumber: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var binding: ActivityMobileNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        supportActionBar!!.hide()

        phone_number = findViewById(R.id.phone_number)
        btn_generate_otp = findViewById(R.id.btn_generate_otp)
        mobile_no_loading = findViewById(R.id.mobile_no_loading)
        country_code = findViewById(R.id.country_code)
        back_mobile_no = findViewById(R.id.back_mobile_no)
        country_code.registerCarrierNumberEditText(phone_number)

        back_mobile_no.setOnClickListener {
            val intent = Intent(this, Login_And_SignUp::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        btn_generate_otp.setOnClickListener {

            login()
        }
    }
    private fun login() {

        if (VaildationNumber()) {
            mobile_no_loading.visibility = View.VISIBLE
            btn_generate_otp.visibility = View.INVISIBLE
            number = phone_number.text?.trim().toString()
            ccode = country_code.selectedCountryCode.trim()
            pnumber = "+${country_code.fullNumber}"

            sendVerificationCode(pnumber)

        } else {

        }

    }


    private fun sendVerificationCode(number: String) {
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
            binding.mobileNoLoading.visibility = View.GONE
            binding.btnGenerateOtp.visibility = View.VISIBLE
            /* startActivity(Intent(applicationContext,Mobile_Number_OTP::class.java))
             finish()*/
        }
        override fun onVerificationFailed(e: FirebaseException) {
            mobile_no_loading.visibility = View.GONE
            btn_generate_otp.visibility = View.VISIBLE
            Toast.makeText(this@Mobile_Number,"Try again", Toast.LENGTH_SHORT).show()
        }
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
            mobile_no_loading.visibility = View.GONE
            btn_generate_otp.visibility = View.VISIBLE



            val intent = Intent(applicationContext, Mobile_Number_OTP::class.java)
            intent.putExtra("storedVerificationId", storedVerificationId)
            intent.putExtra("resendToken",resendToken)
            intent.putExtra("pnumber",pnumber)
            startActivity(intent)
            finish()
        }
    }

    private fun VaildationNumber(): Boolean {
        val phone_number = phone_number.text.toString()

        if (binding.phoneNumber.text.toString() == "") {
            binding.phoneNumberField.error = "Phone Number is required"
            binding.phoneNumber.requestFocus()
            return false
        }else if (!Patterns.PHONE.matcher(phone_number).matches()) {
            binding.phoneNumberField.error = "Phone Number is invaild"
            binding.phoneNumber.requestFocus()
            return false
        }else if (binding.phoneNumber.text.toString().length > 11){
            binding.phoneNumberField.error = "Phone Number is invaild"
            binding.phoneNumber.requestFocus()
            return false
        } else if (binding.phoneNumber.text.toString().length < 10){
            binding.phoneNumberField.error = "Phone Number is invaild"
            binding.phoneNumber.requestFocus()
            return false
        } else {
            binding.phoneNumberField.isErrorEnabled = false
        }
        return true
    }

}