package com.example.hairbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hairbook.databinding.LayoutAppointmentItemBinding
import com.example.hairbook.databinding.LayoutAppointmentItemDeleteBinding
import com.example.hairbook.databinding.LayoutServicesItemBinding
import com.example.hairbook.models.Appointment
import com.example.hairbook.models.AppointmentItem
import com.example.hairbook.models.ServiceItem

class AppointmentAdapterUser(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Appointment> = ArrayList()

    interface OnItemClickListener {
        fun onItemClick(view: View , item: Appointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppointmentViewUserHolder(
            LayoutAppointmentItemDeleteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AppointmentViewUserHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class AppointmentViewUserHolder constructor(
        private val binding: LayoutAppointmentItemDeleteBinding,listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.run {
                this.listener = listener
            }
        }
        fun bind(appointment: Appointment) {
            binding.appointment = appointment
            binding.serviceName.text = appointment.type
            binding.appointmentClientName.text = appointment.name
            binding.appointmentDate.text = appointment.date
            binding.appointmentTime.text = appointment.time
        }

    }

    fun initList(appointmentItemsList: List<Appointment>) {
        items = appointmentItemsList
        this.notifyDataSetChanged()
    }

}