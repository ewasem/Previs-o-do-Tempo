package com.example.previsaodotempo.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi


data class Daily(
    val dt: Long = 0,
    val temp: Temp,
    val humidity: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val clouds: Int,
    val uvi: Double,
    val pop: Double,
    val weather: List<Weather>

): Parcelable {
    @RequiresApi(Build.VERSION_CODES.M)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readTypedObject(Temp)!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.createTypedArrayList(Weather)!!
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dt)
        parcel.writeTypedObject(temp, 0)
        parcel.writeInt(humidity)
        parcel.writeDouble(wind_speed)
        parcel.writeInt(wind_deg)
        parcel.writeInt(clouds)
        parcel.writeDouble(uvi)
        parcel.writeDouble(pop)
        parcel.writeTypedList(weather)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Daily> {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun createFromParcel(parcel: Parcel): Daily {
            return Daily(parcel)
        }

        override fun newArray(size: Int): Array<Daily?> {
            return arrayOfNulls(size)
        }
    }
}