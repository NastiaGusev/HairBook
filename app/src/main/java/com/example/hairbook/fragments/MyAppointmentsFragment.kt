package com.example.hairbook.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hairbook.adapters.AppointmentAdapterUser
import com.example.hairbook.databinding.FragmentMyAppointmentsBinding
import com.example.hairbook.models.Appointment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MyAppointmentsFragment : Fragment(), AppointmentAdapterUser.OnItemClickListener {

    private lateinit var binding: FragmentMyAppointmentsBinding
    var appointmentArray: ArrayList<Appointment> = ArrayList()
    private var appointmentAdapterUser = AppointmentAdapterUser(this)
    private var phone: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView()
        binding.findBTN.setOnClickListener {
            if (!binding.textInputPhone.text.isNullOrEmpty()) {
                phone = binding.textInputPhone.text.toString()
                getAppointments()
            } else {
                Toast.makeText(context, "Enter your phone", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initRecycleView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = appointmentAdapterUser
        }
    }

    private fun getAppointments() {
        appointmentArray.clear()

        val database =
            FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("users").child(phone)

        myRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val appointment = ds.getValue<Appointment>()
                        if (appointment != null) {
                            appointmentArray.add(appointment)
                        }
                    }
                    if (appointmentArray.isNotEmpty()) {
                        appointmentAdapterUser.initList(appointmentArray)
                    } else {
                        Toast.makeText(
                            context,
                            "There are no appointments with this phone number!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Database error - cant make appointment
                }
            })
    }

    override fun onItemClick(view: View, item: Appointment) {
        alertDialogFunction(item)
    }

    private fun alertDialogFunction(item: Appointment) {
        //use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Delete Appointment")
        builder.setMessage("Are you sure you want to delete this appointment?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            Toast.makeText(context, "Appointment deleted!", Toast.LENGTH_LONG).show()
            deleteAppointment(item)
            dialogInterface.dismiss()
        }

        //performing cancel action
        builder.setNeutralButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        //create alert dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false) // will not allow user to cancel after
        alertDialog.show()
    }

    private fun deleteAppointment(item: Appointment) {
        appointmentArray.remove(item)
        appointmentAdapterUser.initList(appointmentArray)
        //remove from database
        val database =
            FirebaseDatabase.getInstance("https://hairbook-d5221-default-rtdb.firebaseio.com/")
        val makeKey = "${item.date}${item.time}"
        database.getReference("users").child(phone).child(makeKey).removeValue()
        database.getReference(item.date!!).child(item.time!!).removeValue()
    }
}