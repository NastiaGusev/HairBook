package com.example.hairbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hairbook.R
import com.example.hairbook.adapters.AppointmentAdapter
import com.example.hairbook.adapters.ServiceAdapter
import com.example.hairbook.databinding.FragmentHairdresserBinding
import com.example.hairbook.databinding.FragmentHomeBinding
import com.example.hairbook.models.AppointmentItem
import com.example.hairbook.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HairdresserFragment : Fragment() {
    private lateinit var binding: FragmentHairdresserBinding
    private var appointmentAdapter = AppointmentAdapter()

    var appointmentArray: ArrayList<AppointmentItem> = ArrayList()
    var currentDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHairdresserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appointmentChangeHours.setOnClickListener {
            val directions =
                HairdresserFragmentDirections.actionHairdresserFragmentToChangeHoursFragment(currentDate)
            findNavController().navigate(directions)
        }
        binding.pickDateBack.setOnClickListener {
            val directions =
                HairdresserFragmentDirections.actionHairdresserFragmentToHomeFragment2()
            findNavController().navigate(directions)
        }
        initRecycleView()
        createCalender()
    }

    private fun initRecycleView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = appointmentAdapter
        }
    }

    private fun createCalender() {
        /* starts before 1 month from now */
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, 0)

        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        currentDate = simpleDateFormat.format(startDate.time)
        getAppointments()

        /* ends after 1 month from now */
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 2)

        val horizontalCalendar = HorizontalCalendar.Builder(activity, binding.calendarView.id)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                //do something
                if (date != null) {
                    currentDate = simpleDateFormat.format(date.time)
                    getAppointments()
                }
            }
        }
    }

    private fun getAppointments() {
        appointmentArray.clear()

        val database =
            FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(currentDate)

        myRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val user = ds.getValue<User>()
                        if(user!!.name!= "hairdresser"){
                            val appointment =
                                AppointmentItem(ds.key.toString(), user!!.type, user.name, user.phone)
                            appointmentArray.add(appointment)
                        }
                    }
                    appointmentAdapter.changeLists(appointmentArray)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Database error - cant make appointment
                }
            })
    }
}
