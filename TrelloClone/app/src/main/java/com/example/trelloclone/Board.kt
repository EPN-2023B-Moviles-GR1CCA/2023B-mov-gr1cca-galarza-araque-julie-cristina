package com.example.trelloclone

import android.os.Parcel
import android.os.Parcelable

data class Board (
    val name: String = "",
    val image: String = "",
    val createBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList()
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!

    )
}