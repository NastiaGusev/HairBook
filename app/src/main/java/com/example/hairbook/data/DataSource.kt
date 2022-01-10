package com.example.hairbook.data

import com.example.hairbook.R
import com.example.hairbook.models.ServiceItem

class DataSource {

    companion object {

        fun createDataSet(): ArrayList<ServiceItem> {
            val list = ArrayList<ServiceItem>()
            list.add(
                ServiceItem(
                    1,
                    R.drawable.ic_fringe,
                    "Haircut",
                    "woman"
                )
            )

            list.add(
                ServiceItem(
                    2,
                    R.drawable.ic_beard,
                    "Haircut",
                    "man"
                )
            )
            return list
        }
    }
}