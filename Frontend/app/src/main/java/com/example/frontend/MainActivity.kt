package com.example.frontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        val phoneTv = findViewById<TextView>(R.id.phoneTv)
        if (firebaseUser == null) {
            //logged out
            startActivity(Intent(this, PhoneAuthentication::class.java))
            finish()
        } else {
            val phone = firebaseUser.phoneNumber
            //Set phone number
            phoneTv.text = phone

        }
    }
}