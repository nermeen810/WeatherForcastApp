package com.example.weatherforcast.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.databinding.HoursItemBinding
import com.example.weatherforcast.model.Hourly
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourlyAdapter(private val hourlyList: ArrayList<Hourly>)  : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>()  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): HourlyAdapter.HourlyViewHolder {
        return HourlyViewHolder(
            HoursItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    fun updateHours(newHourlyList: List<Hourly>) {
        hourlyList.clear()
        hourlyList.addAll(newHourlyList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return hourlyList.size
    }

    override fun onBindViewHolder(holder: HourlyAdapter.HourlyViewHolder, position: Int) {
        holder.binding.dailyHour.text =timeFormat( hourlyList[position].dt.toInt())
        holder.binding.dailyTemb.text=(hourlyList[position].temp.toInt()).toString()+"°c"
        Picasso.get().load(iconLinkgetter(hourlyList[position].weather.get(0).icon)).into(holder.binding.dailyImg)

        // holder.binding.dailyImg.setImageResource(hourlyList[position].)
    }
    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }
    fun iconLinkgetter(iconName:String):String="https://openweathermap.org/img/wn/"+iconName+"@2x.png"
    inner class HourlyViewHolder constructor(val binding: HoursItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}