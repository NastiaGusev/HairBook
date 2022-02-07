package com.example.hairbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hairbook.databinding.LayoutAppointmentItemBinding
import com.example.hairbook.databinding.LayoutServicesItemBinding
import com.example.hairbook.models.AppointmentItem
import com.example.hairbook.models.ServiceItem

class AppointmentAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<AppointmentItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppointmentViewHolder(
            LayoutAppointmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AppointmentViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class AppointmentViewHolder constructor(
        private val binding: LayoutAppointmentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appointmentItem: AppointmentItem) {
            binding.serviceName.text = appointmentItem.serviceName
            binding.appointmentClientName.text = appointmentItem.clientName
            binding.appointmentClientphone.text = appointmentItem.clientPhone
            binding.appointmentTime.text = appointmentItem.time
        }
    }

    fun initList(appointmentItemsList: List<AppointmentItem>) {
        items = appointmentItemsList
    }

    fun changeLists(appointmentItemsList: List<AppointmentItem>) {
        items = appointmentItemsList
        this.notifyDataSetChanged()
    }

}