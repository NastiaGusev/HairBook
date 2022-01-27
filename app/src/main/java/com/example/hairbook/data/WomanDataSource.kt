package com.example.hairbook.data

import com.example.hairbook.R
import com.example.hairbook.models.ServiceItem

class WomanDataSource {

    companion object {

        fun createDataSet(): ArrayList<ServiceItem> {
            val list = ArrayList<ServiceItem>()
            list.add(
                ServiceItem(
                    0,
                    R.drawable.ic_scissors,
                    "HAIRCUT",
                    "woman"
                )
            )
            list.add(
                ServiceItem(
                    1,
                    R.drawable.ic_hair_dryer,
                    "STYLING",
                    "woman"
                )
            )
            list.add(
                ServiceItem(
                    2,
                    R.drawable.ic_hair_straightener,
                    "KERATIN",
                    "woman"
                )
            )
            list.add(
                ServiceItem(
                    3,
                    R.drawable.ic_hair_dye,
                    "COLOR",
                    "woman"
                )
            )
            list.add(
                ServiceItem(
                    4,
                    R.drawable.ic_wig,
                    "EXTENSIONS",
                    "woman"
                )
            )
            return list


        }
    }
}