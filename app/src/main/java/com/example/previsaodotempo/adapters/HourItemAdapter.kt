package com.example.previsaodotempo.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.R
import com.example.previsaodotempo.models.Hourly
import com.example.previsaodotempo.models.Minutely
import com.example.previsaodotempo.models.Weather
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_hour.view.*
import kotlinx.android.synthetic.main.item_minute.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

open class HourItemAdapter(
    private val context: Context,
    private var list: ArrayList<Hourly>,
    private var weather: ArrayList<Weather>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_hour,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        val weather = weather[position]


        if (holder is MyViewHolder) {


            var hora = unixTime(model.dt)
            var data = unixDate(model.dt)
            holder.itemView.tv_hora_hora_activity.text = hora
            holder.itemView.tv_data_hora_activity.text = data


            val temp = model.temp.roundToInt()
            holder.itemView.tv_temp_hora_activity.text = temp.toString() + "°C"
            holder.itemView.tv_desc_hora_activity.text = weather.description.capitalize()

            val sens = model.feels_like.roundToInt()
            holder.itemView.tv_sens_term_hora_activity.text = sens.toString() + "°C"
            holder.itemView.tv_umidade_hora_activity.text = model.humidity.toString() + "%"


            when (weather.icon) {
                "01d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_01d)
                "02d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_02d)
                "03d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_03d)
                "04d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_04d)
                "09d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_09d)
                "10d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_10d)
                "11d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_11d)
                "13d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_13d)
                "50d" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_50d)

                "01n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_01n)
                "02n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_02n)
                "03n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_03n)
                "04n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_04n)
                "09n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_09n)
                "10n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_10n)
                "11n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_11n)
                "13n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_13n)
                "50n" -> holder.itemView.iv_icon_hora.setImageResource(R.drawable.ic_50d)

            }
        }


    }


    override fun getItemCount(): Int {
        return list.size

    }


    /**
     * A function for OnClickListener where the Interface is the expected parameter..
     */


    /**
     * An interface for onclick items.
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun unixTime(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun unixDate(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("dd/MM", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}