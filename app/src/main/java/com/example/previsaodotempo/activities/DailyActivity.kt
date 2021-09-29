package com.example.previsaodotempo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.previsaodotempo.R
import com.example.previsaodotempo.adapters.DailyItemAdapter

import com.example.previsaodotempo.models.Daily
import com.example.previsaodotempo.models.Temp

import com.example.previsaodotempo.models.Weather
import com.example.previsaodotempo.models.WeatherResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_daily.*
import java.io.Serializable


class DailyActivity : AppCompatActivity() {private lateinit var mList: ArrayList<Daily>
    private lateinit var mWeather: java.util.ArrayList<Weather>
    private lateinit var mTemp: ArrayList<Temp>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)


        if (intent.hasExtra(MainActivity.DAILY)) {
       mList = intent.getParcelableArrayListExtra(MainActivity.DAILY)




            if (intent.hasExtra(MainActivity.WEATHER_DAILY)) {
            mWeather = intent.getParcelableArrayListExtra(MainActivity.WEATHER_DAILY)
        }

        if (intent.hasExtra(MainActivity.TEMP_DAILY)) {
            mTemp = intent.getParcelableArrayListExtra(MainActivity.TEMP_DAILY)
        }

        populateBoardsListToUI(mList, mWeather, mTemp)
    }}

    fun populateBoardsListToUI(dailyList: ArrayList<Daily>, weatherList: ArrayList<Weather>, tempList: ArrayList<Temp>) {




        rv_daily_list.layoutManager = LinearLayoutManager(this)
        rv_daily_list.setHasFixedSize(true)
        val adapter = DailyItemAdapter(this, dailyList, weatherList, tempList)
        rv_daily_list.adapter = adapter

    }




}