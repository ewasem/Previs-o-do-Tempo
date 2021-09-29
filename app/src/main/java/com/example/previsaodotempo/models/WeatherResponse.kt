package com.example.previsaodotempo.models

import java.io.Serializable

data class WeatherResponse (
    val lon: Double,
    val lat: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val current: Current,
    val minutely: List<Minutely>,
    val hourly: List<Hourly>,
    val daily: List<Daily>,

) : Serializable
