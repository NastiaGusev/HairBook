package com.example.hairbook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hairbook.R
import com.example.hairbook.databinding.ActivityMainBinding
import com.example.hairbook.databinding.ActivityServicesBinding

class ServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

    }
}