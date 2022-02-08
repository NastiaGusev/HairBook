package com.example.hairbook.fragments

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hairbook.adapters.AppointmentAdapterUser
import com.example.hairbook.databinding.FragmentMyAppointmentsBinding
import com.example.hairbook.models.Appointment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MyAppointmentsFragment : Fragment(), AppointmentAdapterUser.OnItemClickListener {

    private lateinit var binding: FragmentMyAppointmentsBinding
    var appointmentArray: ArrayList<Appointment> = ArrayList()
    private var appointmentAdapterUser = AppointmentAdapterUser(this)
    private var phone: String = ""
    private var storedVerificationId: String? = ""
    private lateinit var credential:PhoneAuthCredential


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initRecycleView()
        binding.findBTN.setOnClickListener {
            if (!binding.textInputPhone.text.isNullOrEmpty() && binding.textInputLayoutPhone.visibility ==  View.VISIBLE) {
                phone = binding.textInputPhone.text.toString()
                binding.textInputLayoutPhone.visibility = View.GONE
                binding.textInputLayoutCode.visibility = View.VISIBLE
                binding.textInfo.text = "Enter the code that you received"
                binding.findBTN.text = "Submit"
                firebaseAuth()
               // getAppointments()
            } else {
                Toast.makeText(context, "Enter your phone", Toast.LENGTH_LONG).show()
            }
            if(!binding.textInputCode.text.isNullOrEmpty() && binding.textInputLayoutCode.visibility ==  View.VISIBLE){
                getCodeFromUser()
            }else {
                Toast.makeText(context, "Enter the code", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun firebaseAuth(){
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber("+972266666666")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.d("auth00", "code failed:")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d("auth00", "onCodeSent:")

                    // Save verification ID and resending token so we can use them later
                    storedVerificationId = verificationId
                    //resendToken = token
                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun getCodeFromUser(){
        val userCode = binding.textInputCode.text.toString()
        Log.d("TAG","${storedVerificationId!!} user code $userCode")
        credential = PhoneAuthProvider.getCredential(storedVerificationId!!, userCode)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("auth00", "signInWithCredential:success")

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("auth00", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
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
                    appointmentAdapterUser.initList(appointmentArray)
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
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Toast.makeText(context, "Appointment deleted!", Toast.LENGTH_LONG).show()
            deleteAppointment(item)
            dialogInterface.dismiss()
        }

        //performing cancel action
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        //create alert dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false) // will not allow user to cancel after
        alertDialog.show()
    }

    private fun deleteAppointment(item: Appointment){
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