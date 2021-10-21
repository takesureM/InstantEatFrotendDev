package com.example.frontend

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class PhoneAuthentication : AppCompatActivity() {



    // if code sending failed, will resend
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId: String? = null

    private lateinit var firebaseAuth: FirebaseAuth

    private var TAG = "MAIN_TAG"

    //progress bar dialog
    private lateinit var progressDialog: ProgressDialog

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_authentication)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        val phoneL1 = findViewById<LinearLayout>(R.id.phoneL1)
        val codeL1 = findViewById<LinearLayout>(R.id.codeL1)
        val phoneContinueBtn = findViewById<Button>(R.id.phoneContinueBtn)
        val codeSubmitBtn = findViewById<Button>(R.id.codeSubmitBtn)
        val resendCodeTv = findViewById<TextView>(R.id.resendCodeTv)
        val codeSendCodeTv = findViewById<TextView>(R.id.codeSentDescriptionTv)

        val phoneEt = findViewById<EditText>(R.id.phoneEt)
        val codeEt = findViewById<EditText>(R.id.codeEt)

        phoneL1.visibility = View.VISIBLE
        codeL1.visibility = View.GONE



        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.d(TAG,"onVerificationCompleted")
                signInWithPhoneAuthCredential(phoneAuthCredential)

            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Log.d(TAG,"onVerificationFailed: ${e.message}")
                Toast.makeText(this@PhoneAuthentication, "${e.message}", Toast.LENGTH_SHORT).show()

            }


            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG, "onCodeSent: $verificationId")
                mVerificationId =verificationId
                forceResendingToken = token
                progressDialog.dismiss()

                Log.d(TAG,"onCodeSent: $verificationId")

                //Hide phone layout, show code layout
                phoneL1.visibility = View.GONE
                codeL1.visibility = View.VISIBLE
                Toast.makeText(this@PhoneAuthentication, "Verification code sent....", Toast.LENGTH_SHORT).show()
                codeSendCodeTv.text = "Please type the verification code we send to ${phoneEt.text.toString().trim()}"

            }


        }
        // phoneContinueBtn click: input phone number, validate, start phone authentication/login
        phoneContinueBtn.setOnClickListener {
            val phone = phoneEt.text.toString().trim()
            //validate phone number
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this@PhoneAuthentication, "Please enter phone number", Toast.LENGTH_SHORT).show()
            } else {
                startPhoneNumberVerification(phone)
            }
        }

        //resendCodeTv click: input phone number, validate, start phone authentication/
        resendCodeTv.setOnClickListener {
            val phone = phoneEt.text.toString().trim()
            //validate phone number
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this@PhoneAuthentication, "Please enter phone number", Toast.LENGTH_SHORT).show()
            } else {
                resendVerificationCode(phone, forceResendingToken!!)
            }
        }
        // codeSubmitBtn click: input phone number, validate, verify phone number with verification code
        codeSubmitBtn.setOnClickListener {
            //send information to the api server
            // Input verification code
            val code = codeEt.text.toString().trim()
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this@PhoneAuthentication, "Please enter verification code", Toast.LENGTH_SHORT).show()
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code)



            }
        }


    }


    private fun startPhoneNumberVerification(phone: String) {
        Log.d(TAG,"startPhoneNumberVerification: $phone")
        progressDialog.setMessage("Verifying Phone Number.....")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode(phone: String, token:PhoneAuthProvider.ForceResendingToken) {
        progressDialog.setMessage("Resending Code.....")
        progressDialog.show()

        Log.d(TAG,"resendVerificationCode: $phone")

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks!!)
            .setForceResendingToken(token)
            .build()

    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String)  {

        Log.d(TAG,"verifyPhoneNumberWithCode: $verificationId $code")

        progressDialog.setMessage("Verifying Code.....")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential)  {

        Log.d(TAG,"verifyPhoneNumberWithCode: $credential")

        progressDialog.setMessage("Logging ")

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                // login success
                progressDialog.dismiss()
                val phone = firebaseAuth.currentUser!!.phoneNumber
                Toast.makeText(this, "Logged In as $phone", Toast.LENGTH_SHORT).show()

                //Start main activity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // login failed
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

            }


    }
}