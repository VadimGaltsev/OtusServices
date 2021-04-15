package com.otus.services

import android.os.Parcel
import android.os.Parcelable

data class Circle(var x: Int = 0, var y: Int = 0, var radius: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(x)
        parcel.writeInt(y)
        parcel.writeInt(radius)
    }

    fun readFromParcel(parcel: Parcel) {
        x = parcel.readInt()
        y = parcel.readInt()
        radius = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Circle> {

        override fun createFromParcel(parcel: Parcel): Circle {
            return Circle(parcel)
        }

        override fun newArray(size: Int): Array<Circle?> {
            return arrayOfNulls(size)
        }
    }
}