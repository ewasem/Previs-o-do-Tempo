package com.example.previsaodotempo.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.previsaodotempo.Constants
import com.example.previsaodotempo.R
import com.example.previsaodotempo.adapters.MinuteItemAdapter
import com.example.previsaodotempo.models.Minutely
import kotlinx.android.synthetic.main.activity_minute.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MinuteActivity : AppCompatActivity() {


    private lateinit var mList: java.util.ArrayList<Minutely>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minute)

        if (intent.hasExtra(MainActivity.MINUTE)) {
            mList = intent.getParcelableArrayListExtra(MainActivity.MINUTE)
        }

        populateBoardsListToUI(mList)
    }

    fun populateBoardsListToUI(minutetelyList: ArrayList<Minutely>) {




        rv_minute_list.layoutManager = LinearLayoutManager(this)
        rv_minute_list.setHasFixedSize(true)
        val adapter = MinuteItemAdapter(this, minutetelyList)
        rv_minute_list.adapter = adapter

    }




}