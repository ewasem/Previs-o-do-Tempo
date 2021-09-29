package com.example.previsaodotempo.network

import com.example.previsaodotempo.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("2.5/onecall")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        
        @Query("units") units: String?,
        @Query("appid") appid: String?,
        @Query("lang") lang: String?


        ): Call<WeatherResponse>
}