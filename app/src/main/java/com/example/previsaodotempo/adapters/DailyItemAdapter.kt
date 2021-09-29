package com.example.previsaodotempo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.R
import com.example.previsaodotempo.models.Daily
import com.example.previsaodotempo.models.Temp
import com.example.previsaodotempo.models.Weather
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_daily.view.*
import kotlinx.android.synthetic.main.item_hour.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

open class DailyItemAdapter(
    private val context: Context,
    private var list: ArrayList<Daily>,
    private var weather: ArrayList<Weather>,
    private var temp: ArrayList<Temp>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_daily,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        val weather = weather[position]

        val temp = temp[position]


        if (holder is MyViewHolder) {



            var data = unixDate(model.dt)

            holder.itemView.tv_data_daily_activity.text = data


            val temp_min = temp.min.roundToInt()
            val temp_max = temp.max.roundToInt()
            holder.itemView.tv_temp_min_daily_activity.text = "$temp_min°C"
            holder.itemView.tv_temp_max_daily_activity.text = "$temp_max°C"
            holder.itemView.tv_desc_daily_activity.text = weather.description.capitalize()

            var dia = unixDayOFTheWeek((model.dt))

            holder.itemView.tv_dia_daily_activity.text = dia

            holder.itemView.tv_umidade_daily_activity.text = model.humidity.toString() + "%"

            val uv = model.uvi.roundToInt()
            var indiceUv: String
            if (uv < 3) {
                indiceUv = "BAIXO"
            } else if (uv < 6) {
                indiceUv = "MODERADO"
            } else if (uv < 8) {
                indiceUv = "ALTO"
            } else if (uv < 11) {
                indiceUv = "MUITO ALTO"
            } else {
                indiceUv = "EXTREMO"
            }
            holder.itemView.tv_uv_daily_activity.text = uv.toString() + " - " + indiceUv

            var wind = (model.wind_speed * 3.6).roundToInt()

            holder.itemView.tv_vento_daily_activity.text = wind.toString() + " km/h"

            var prob = (model.pop * 100).toInt()
            /*val df = DecimalFormat("##")
            df.roundingMode = RoundingMode.UNNECESSARY
            df.format(prob)*/


            holder.itemView.tv_prob_chuva_daily.text = prob.toString() + "%"

            var clouds = model.clouds
            holder.itemView.tv_clouds.text = clouds.toString() + "%"

            var direction: String
            if (model.wind_deg < 11.26) {
                direction = "Norte"
            } else if (model.wind_deg < 33.76) {
                direction = "Norte-nordeste"
            } else if (model.wind_deg < 56.26) {
                direction = "Nordeste"
            } else if (model.wind_deg < 78.76) {
                direction = "Este-nordeste"
            } else if (model.wind_deg < 101.76) {
                direction = "Leste"
            } else if (model.wind_deg< 123.76) {
                direction = "Este-sudeste"
            } else if (model.wind_deg < 146.76) {
                direction = "Sudeste"
            } else if (model.wind_deg < 168.76) {
                direction = "Sul-sudeste"
            } else if (model.wind_deg < 191.76) {
                direction = "Sul"
            } else if (model.wind_deg < 213.76) {
                direction = "Sul-sudoeste"
            } else if (model.wind_deg < 236.76) {
                direction = "Sudoeste"
            } else if (model.wind_deg < 258.76) {
                direction = "Oeste-sudoeste"
            } else if (model.wind_deg < 281.76) {
                direction = "Oeste"
            } else if (model.wind_deg < 303.76) {
                direction = "Oeste-noroeste"
            } else if (model.wind_deg < 326.76) {
                direction = "Noroeste"
            } else if (model.wind_deg < 348.76) {
                direction = "Norte-noroeste"
            } else {
                direction = "Norte"
            }
            holder.itemView.tv_wind_direction_daily.text = direction

            when (weather.icon) {
                "01d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_01d)
                "02d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_02d)
                "03d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_03d)
                "04d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_04d)
                "09d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_09d)
                "10d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_10d)
                "11d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_11d)
                "13d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_13d)
                "50d" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_50d)

                "01n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_01n)
                "02n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_02n)
                "03n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_03n)
                "04n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_04n)
                "09n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_09n)
                "10n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_10n)
                "11n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_11n)
                "13n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_13n)
                "50n" -> holder.itemView.iv_icon_daily.setImageResource(R.drawable.ic_50d)

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


    private fun unixDate(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("dd/MM", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun unixDayOFTheWeek(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("EEEE", Locale("pt", "BR"))
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }


}