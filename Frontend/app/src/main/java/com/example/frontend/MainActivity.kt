package com.example.frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.adapter.ClientAdapter
import com.example.frontend.databinding.ActivityMainBinding
import com.example.frontend.repository.Repository
import com.example.frontend.viewModel.MainViewModel
import com.example.frontend.viewModel.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private val myAdapter by lazy { ClientAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()
        setupRecyclerview()

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.addClient(1, "Takesure", "ndjxnjwdnuwd")
        viewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d("Main", response.body().toString())
                Log.d("Main", response.code().toString())
                Log.d("Main", response.message())
            } else {
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })


    }


    private fun setupRecyclerview() {
        binding.recyclerView.adapter = myAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
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