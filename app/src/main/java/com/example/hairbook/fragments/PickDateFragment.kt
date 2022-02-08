package com.example.hairbook.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hairbook.R
import com.example.hairbook.data.ManDataSource
import com.example.hairbook.data.WomanDataSource
import com.example.hairbook.databinding.FragmentPickDateBinding
import com.example.hairbook.models.ServiceItem
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.database.*
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.model.CalendarItemStyle
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarPredicate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PickDateFragment : Fragment() {

    private val args: PickDateFragmentArgs by navArgs()
    private val serviceId: Long by lazy(LazyThreadSafetyMode.NONE) { args.serviceItemId }
    private val serviceGender: String by lazy(LazyThreadSafetyMode.NONE) { args.serviceItemGender }
    private lateinit var service: ServiceItem
    private lateinit var binding: FragmentPickDateBinding

    var allTimeArray: ArrayList<TextView> = ArrayList()
    var timeArray: ArrayList<TextView> = ArrayList()
    var unSelectTimeArray: ArrayList<TextView> = ArrayList()
    var currentTime: String = ""
    var currentDate: String = ""

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pickDateBack.setOnClickListener {
            findNavController().navigateUp()
        }

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
        addDataSet()
        createCalender()

        binding.serviceTitle.text = service.serviceName
        binding.serviceImage.setImageResource(service.image)

        binding.BTNSubmit.setOnClickListener {
            if (currentTime.isNotEmpty()) {
                val directions = PickDateFragmentDirections.actionPickDateFragmentToSubmitFragment(
                    currentTime,
                    currentDate,
                    service.serviceName,
                    service.image,
                    service.gender
                )
                findNavController().navigate(directions)
            } else {
                Toast.makeText(context, "You have to pick a time!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addDataSet() {
        lateinit var data: ArrayList<ServiceItem>
        if (serviceGender == "woman") {
            data = WomanDataSource.createDataSet()
        } else if (serviceGender == "man") {
            data = ManDataSource.createDataSet()
        }
        service = data[serviceId.toInt()]
    }

    private fun createCalender() {
        /* starts before 1 month from now */
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH, 0)

        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        currentDate = simpleDateFormat.format(startDate.time)
        getAvailableHours()

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
                    currentTime = ""
                    currentDate = simpleDateFormat.format(date.time)
                    getAvailableHours()
                }
            }
        }
    }

    private fun getAvailableHours() {
        unSelectTimeArray.clear()
        timeArray.clear()

        val database =
            FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(currentDate)

        myRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (tv in allTimeArray) {
                        if (dataSnapshot.child(tv.text.toString()).exists()) {
                            unSelectTimeArray.add(tv)
                        } else {
                            timeArray.add(tv)
                        }
                    }
                    updateHoursView()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Database error - cant make appointment
                    unSelectTimeArray.addAll(
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

    private fun updateHoursView() {
        setUnClickable()
        defaultViewTime()
        for (textView in timeArray) {
            textView.setOnClickListener {
                onClickTime(textView)
            }
        }
        submitButtonView()
    }

    private fun onClickTime(textView: TextView) {
        currentTime = textView.text.toString()
        submitButtonView()
        defaultViewTime()
        selectedViewTime(textView)
    }

    private fun submitButtonView() {
        if (currentTime.isNotEmpty()) {
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

    private fun setUnClickable() {
        for (textView in unSelectTimeArray) {
            textView.isClickable = false
            textView.background =
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.background_time_unselectable
                )
            textView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
        }
    }

    private fun defaultViewTime() {
        for (textView in timeArray) {
            textView.isClickable = true
            textView.background =
                AppCompatResources.getDrawable(binding.root.context, R.drawable.background_time)
            textView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
        }
    }

    private fun selectedViewTime(textView: TextView) {
        textView.background = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.background_time_selected
        )
        textView.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
    }
}