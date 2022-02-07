package com.example.hairbook.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.hairbook.databinding.ActivityFinishBinding
import com.example.hairbook.fragments.SubmitFragmentArgs

class FinishActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFinishBinding

    private val args: FinishActivityArgs by navArgs()
    private val serviceName: String by lazy(LazyThreadSafetyMode.NONE) { args.serviceName }
    private val dateAndTime: String by lazy(LazyThreadSafetyMode.NONE) { args.dateAndTime }
    private val clientName: String by lazy(LazyThreadSafetyMode.NONE) { args.clientName }
    private val clientPhone: String by lazy(LazyThreadSafetyMode.NONE) { args.clientPhone }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewServiceName.text = serviceName
        binding.textViewDateAndTime.text = dateAndTime
        binding.textViewName.text = clientName
        binding.textViewPhone.text = clientPhone
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}