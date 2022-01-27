package com.example.hairbook.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hairbook.R
import com.example.hairbook.data.ManDataSource
import com.example.hairbook.data.WomanDataSource
import com.example.hairbook.databinding.FragmentChooseServiceBinding
import com.example.hairbook.databinding.FragmentPickDateBinding
import com.example.hairbook.models.ServiceItem
import com.google.android.material.transition.MaterialContainerTransform
import java.util.*
import kotlin.collections.ArrayList

class PickDateFragment : Fragment() {

    private val args: PickDateFragmentArgs by navArgs()
    private val serviceId: Long by lazy(LazyThreadSafetyMode.NONE) { args.serviceItemId }
    private val serviceGender: String by lazy(LazyThreadSafetyMode.NONE) { args.serviceItemGender }
    private lateinit var service: ServiceItem

    private lateinit var binding: FragmentPickDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPickDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun addDataSet() {
        //create posts
        lateinit var data : ArrayList<ServiceItem>
        if(serviceGender == "woman"){
            data = WomanDataSource.createDataSet()
        }else if(serviceGender == "man") {
            data = ManDataSource.createDataSet()
        }
        service = data[serviceId.toInt()]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pickDateBack.setOnClickListener {
            findNavController().navigateUp()
        }
        addDataSet()
        binding.serviceTitle.text = service.serviceName
        binding.serviceImage.setImageResource(service.image)

    }
}