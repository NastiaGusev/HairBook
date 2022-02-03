package com.example.hairbook.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hairbook.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFinishBinding
    private val serviceName = "SERVICE_NAME"
    private val dateAndTime = "DATE_AND_TIME"
    private val name = "NAME"
    private val phone = "PHONE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sn = intent.getStringExtra(serviceName)
        val dt = intent.getStringExtra(dateAndTime)
        val userName = intent.getStringExtra(name)
        val userPhone = intent.getStringExtra(phone)
        binding.textViewServiceName.text = sn
        binding.textViewDateAndTime.text = dt
        binding.textViewName.text = userName
        binding.textViewPhone.text = userPhone
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}