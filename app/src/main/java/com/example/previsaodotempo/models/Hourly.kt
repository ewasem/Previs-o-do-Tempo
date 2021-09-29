package com.example.previsaodotempo.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Hourly(
    val dt: Long= 0,
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val humidity: Int = 0,
    val weather: ArrayList<Weather> = ArrayList()

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.createTypedArrayList(Weather.CREATOR)!!

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dt)
        parcel.writeDouble(temp)
        parcel.writeDouble(feels_like)
        parcel.writeInt(humidity)
        parcel.writeTypedList(weather)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hourly> {
        override fun createFromParcel(parcel: Parcel): Hourly {
            return Hourly(parcel)
        }

        override fun newArray(size: Int): Array<Hourly?> {
            return arrayOfNulls(size)
        }
    }
}
