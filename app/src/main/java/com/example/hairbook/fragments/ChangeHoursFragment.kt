package com.example.hairbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hairbook.R
import com.example.hairbook.databinding.FragmentChangeHoursBinding
import com.example.hairbook.databinding.FragmentHairdresserBinding

class ChangeHoursFragment : Fragment() {

    private lateinit var binding: FragmentChangeHoursBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

}