package com.example.previsaodotempo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.previsaodotempo.R
import com.example.previsaodotempo.adapters.HourItemAdapter
import com.example.previsaodotempo.adapters.MinuteItemAdapter
import com.example.previsaodotempo.models.Hourly
import com.example.previsaodotempo.models.Weather
import kotlinx.android.synthetic.main.activity_hour.*

class HourActivity : AppCompatActivity() {
    private lateinit var mList: java.util.ArrayList<Hourly>
    private lateinit var mWeather: java.util.ArrayList<Weather>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hour)


            mList = intent.getParcelableArrayListExtra(MainActivity.HOUR)


        if (intent.hasExtra(MainActivity.WEATHER_HOUR)) {
            mWeather = intent.getParcelableArrayListExtra(MainActivity.WEATHER_HOUR)
        }

        populateBoardsListToUI(mList, mWeather)
    }

    fun populateBoardsListToUI(hourList: ArrayList<Hourly>, weatherList: ArrayList<Weather>) {




        rv_hour_list.layoutManager = LinearLayoutManager(this)
        rv_hour_list.setHasFixedSize(true)
        val adapter = HourItemAdapter(this, hourList, weatherList)
        rv_hour_list.adapter = adapter

    }




}