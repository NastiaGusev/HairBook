package com.example.hairbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hairbook.databinding.FragmentSubmitBinding
import com.example.hairbook.models.Appointment
import com.example.hairbook.models.User
import com.google.firebase.database.FirebaseDatabase

class SubmitFragment : Fragment() {

    private val args: SubmitFragmentArgs by navArgs()
    private val serviceName: String by lazy(LazyThreadSafetyMode.NONE) { args.serviceName }
    private val serviceImage: Int by lazy(LazyThreadSafetyMode.NONE) { args.serviceImage }
    private val serviceGender: String by lazy(LazyThreadSafetyMode.NONE) { args.serviceGender }
    private val currentTime: String by lazy(LazyThreadSafetyMode.NONE) { args.time }
    private val currentDate: String by lazy(LazyThreadSafetyMode.NONE) { args.date }

    private lateinit var binding: FragmentSubmitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubmitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.serviceImage.setImageResource(serviceImage)
        binding.textViewServiceName.text = serviceName
        val setTime = "$currentDate at $currentTime"
        binding.textViewDateAndTime.text = setTime

        binding.submitBTNConfirm.setOnClickListener {
            val name = binding.textInputName.text.toString()
            val phone = binding.textInputPhone.text.toString()
            if(name.isNotEmpty() && phone.isNotEmpty()){
                saveAppointment(name, phone)
            }else {
                Toast.makeText(context, "You have to enter name and phone", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveAppointment(name: String, phone: String){
        val database = FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(currentDate)
        myRef.child(currentTime).setValue(User(name,phone,serviceGender, serviceName))

        val myRefUser = database.getReference("users")
        val makeKey = "$currentDate$currentTime"
        myRefUser.child(phone).child(makeKey).setValue(Appointment(makeKey,currentDate,currentTime,name, serviceName))

        val directions = SubmitFragmentDirections.actionSubmitFragmentToFinishActivity(
            serviceName,
            "$currentDate at $currentTime",
            name,
            phone)
        findNavController().navigate(directions)
        Toast.makeText(context, "Your appointment has been saved!", Toast.LENGTH_LONG).show()
    }

}