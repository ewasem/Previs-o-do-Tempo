package com.example.previsaodotempo.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class Minutely(
    val dt: Long,
    val precipitation: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dt)
        parcel.writeDouble(precipitation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Minutely> {
        override fun createFromParcel(parcel: Parcel): Minutely {
            return Minutely(parcel)
        }

        override fun newArray(size: Int): Array<Minutely?> {
            return arrayOfNulls(size)
        }
    }
}