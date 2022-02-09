package com.example.hairbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hairbook.R
import com.example.hairbook.databinding.FragmentChangeHoursBinding
import com.example.hairbook.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

class ChangeHoursFragment : Fragment() {

    private val args: ChangeHoursFragmentArgs by navArgs()
    private val currentDate: String by lazy(LazyThreadSafetyMode.NONE) { args.currentDate }

    private lateinit var binding: FragmentChangeHoursBinding

    private var selectedTimeArray: ArrayList<String> = ArrayList()
    private var unselectedTimeArray: ArrayList<String> = ArrayList()

    var hairDresserArray: ArrayList<TextView> = ArrayList()
    var availableArray: ArrayList<TextView> = ArrayList()
    var appointmentsArray: ArrayList<TextView> = ArrayList()
    var allTimeArray: ArrayList<TextView> = ArrayList()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.currentDate.text = currentDate
        initAllTimeArray()
        getAvailableHours()

        binding.checkboxAllDay.setOnCheckedChangeListener { _, isChecked ->
            checkBoxClicked(isChecked)
        }
        binding.BTNSubmit.setOnClickListener {
            saveCancelHours()
        }
        binding.pickDateBack.setOnClickListener {
            val directions =
                ChangeHoursFragmentDirections.actionChangeHoursFragmentToHairdresserFragment()
            findNavController().navigate(directions)
        }
    }

    private fun initAllTimeArray(){
        allTimeArray.addAll(
            listOf(
                binding.timeTxt10,
                binding.timeTxt11,
                binding.timeTxt12,
                binding.timeTxt13,
                binding.timeTxt14,
                binding.timeTxt15,
                binding.timeTxt16,
                binding.timeTxt17,
                binding.timeTxt18,
                binding.timeTxt19,
                binding.timeTxt20,
                binding.timeTxt21
            )
        )
    }

    private fun checkBoxClicked(isChecked: Boolean) {
        for (tv in availableArray) {
            if (isChecked) {
                selectedTimeArray.add(tv.text.toString())
                unselectedTimeArray.remove(tv.text.toString())
                selectedViewTime(tv)
            } else {
                selectedTimeArray.remove(tv.text.toString())
                unselectedTimeArray.add(tv.text.toString())
                unselectedViewTime(tv)
            }
        }
        for (tv in hairDresserArray) {
            if (isChecked) {
                selectedTimeArray.add(tv.text.toString())
                unselectedTimeArray.remove(tv.text.toString())
                selectedViewTime(tv)
            } else {
                selectedTimeArray.remove(tv.text.toString())
                unselectedTimeArray.add(tv.text.toString())
                unselectedViewTime(tv)
            }
        }
        submitButtonView()
    }

    private fun getAvailableHours() {
        appointmentsArray.clear()
        availableArray.clear()

        val database =
            FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(currentDate)

        myRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (tv in allTimeArray) {
                        val time = tv.text.toString()
                        if (dataSnapshot.child(time).exists()) {
                            val user = dataSnapshot.child(time).getValue<User>()
                            if (user!!.name == "hairdresser") {
                                hairDresserArray.add(tv)
                            } else {
                                appointmentsArray.add(tv)
                            }
                        } else {
                            availableArray.add(tv)
                        }
                    }
                    updateHoursView()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Database error - cant make appointment
                    appointmentsArray.addAll(
                        listOf(
                            binding.timeTxt10,
                            binding.timeTxt11,
                            binding.timeTxt12,
                            binding.timeTxt13,
                            binding.timeTxt14,
                            binding.timeTxt15,
                            binding.timeTxt16,
                            binding.timeTxt17,
                            binding.timeTxt18,
                            binding.timeTxt19,
                            binding.timeTxt20,
                            binding.timeTxt21
                        )
                    )
                }
            })
    }

    private fun saveCancelHours() {
        val database =
            FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(currentDate)
        for (time in selectedTimeArray) {
            myRef.child(time).setValue(User("hairdresser", "", "", ""))
        }
        for (time in unselectedTimeArray) {
            myRef.child(time).removeValue()
        }
        Toast.makeText(context, "Your changes have been saved!", Toast.LENGTH_SHORT).show()
        val directions =
            ChangeHoursFragmentDirections.actionChangeHoursFragmentToHairdresserFragment()
        findNavController().navigate(directions)
    }

    private fun updateHoursView() {
        setUnClickableAppointments()
        setDefaultTimeView()
        hairDresserPick()
        for (textView in availableArray) {
            textView.setOnClickListener {
                onClickTime(textView)
            }
        }
        for (textView in hairDresserArray) {
            textView.setOnClickListener {
                onClickTime(textView)
            }
        }
        submitButtonView()
    }

    private fun onClickTime(textView: TextView) {
        val tv = textView.text.toString()
        if (selectedTimeArray.contains(tv)) {
            selectedTimeArray.remove(tv)
            unselectedTimeArray.add(tv)
            unselectedViewTime(textView)
        } else {
            selectedTimeArray.add(tv)
            unselectedTimeArray.remove(tv)
            selectedViewTime(textView)
        }
        submitButtonView()
    }

    private fun submitButtonView() {
        if (selectedTimeArray.isNotEmpty()) {
            binding.BTNSubmit.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.lightGreen
                )
            )
            binding.BTNSubmit.setStrokeColorResource(R.color.lightGreen)
            binding.BTNSubmit.setRippleColorResource(R.color.lightGreen)
        } else {
            binding.BTNSubmit.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.red
                )
            )
            binding.BTNSubmit.setStrokeColorResource(R.color.red)
            binding.BTNSubmit.setRippleColorResource(R.color.red)
        }
    }

    private fun setUnClickableAppointments() {
        for (textView in appointmentsArray) {
            textView.isClickable = false
            textView.background =
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.background_time_unselectable
                )
            textView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
        }
    }

    private fun setDefaultTimeView() {
        for (textView in availableArray) {
            unselectedTimeArray.add(textView.text.toString())
            textView.isClickable = true
            unselectedViewTime(textView)
        }
    }

    private fun hairDresserPick() {
        for (textView in hairDresserArray) {
            selectedTimeArray.add(textView.text.toString())
            selectedViewTime(textView)
            textView.isClickable = true
        }
    }

    private fun selectedViewTime(textView: TextView) {
        textView.background = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.background_time_selected_red
        )
        textView.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
    }

    private fun unselectedViewTime(textView: TextView) {
        textView.background = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.background_time
        )
        textView.setTextColor(
            ContextCompat.getColor(
                binding.root.context, R.color.grey
            )
        )
    }

}