package com.example.hairbook.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.fragment.findNavController
import com.example.hairbook.R
import com.example.hairbook.databinding.ActivityMainBinding
import com.example.hairbook.databinding.FragmentHomeBinding
import com.example.hairbook.databinding.FragmentPickDateBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainBTNGetStarted.setOnClickListener {
            when (binding.radioGroup.checkedRadioButtonId) {
                binding.radioClient.id ->{
                    val directions = HomeFragmentDirections.actionHomeFragment2ToChooseServiceFragment()
                    findNavController().navigate(directions)
                }

                binding.radioHairdresser.id ->{
                    val directions = HomeFragmentDirections.actionHomeFragment2ToHairdresserFragment()
                    findNavController().navigate(directions)
                }

            }

        }
    }
}