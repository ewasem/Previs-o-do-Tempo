package com.example.previsaodotempo.adapters


import android.app.AlertDialog
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.previsaodotempo.R

import com.example.previsaodotempo.models.Minutely
import kotlinx.android.synthetic.main.item_minute.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


open class MinuteItemAdapter(
    private val context: Context,
    private var list: ArrayList<Minutely>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_minute,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]



        if (holder is MyViewHolder) {


            var teste = unixTime(model.dt)
            holder.itemView.tv_hora.text = teste



            val chuva = model.precipitation.roundToInt()
            holder.itemView.tv_chuva_acumulada.text = chuva.toString() + " mm"


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
}