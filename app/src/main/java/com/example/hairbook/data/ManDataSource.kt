package com.example.hairbook.data

import com.example.hairbook.R
import com.example.hairbook.models.ServiceItem

class ManDataSource{
    companion object {
        fun createDataSet(): ArrayList<ServiceItem> {
            val list = ArrayList<ServiceItem>()
            list.add(
                ServiceItem(
                    1,
                    R.drawable.ic_scissors,
                    "HAIRCUT",
                    "man"
                )
            )
            list.add(
                ServiceItem(
                    2,
                    R.drawable.ic_straight_razor,
                    "SHAVE",
                    "man"
                )
            )
            list.add(
                ServiceItem(
                    3,
                    R.drawable.ic_hair_dye,
                    "COLOR",
                    "man"
                )
            )
            return list
        }
    }
}
