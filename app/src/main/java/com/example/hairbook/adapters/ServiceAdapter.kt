package com.example.hairbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hairbook.databinding.LayoutServicesItemBinding
import com.example.hairbook.models.ServiceItem

class ServiceAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ServiceItem> = ArrayList()

    interface OnItemClickListener {
        fun onItemClick(view: View, item: ServiceItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ServiceViewHolder(
            LayoutServicesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ServiceViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class ServiceViewHolder  constructor(
        private val binding: LayoutServicesItemBinding, listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.run {
                this.listener = listener
            }
        }

        fun bind(serviceItem: ServiceItem) {
            binding.serviceItem = serviceItem
            binding.serviceImage.setImageResource(serviceItem.image)
            binding.serviceName.text = serviceItem.serviceName

        }
    }

    fun initList(serviceItemList: List<ServiceItem>) {
        items = serviceItemList
    }


}