package com.example.weatherforcast.view.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.databinding.HoursItemBinding
import com.example.weatherforcast.model.Hourly
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourlyAdapter(private val hourlyList: ArrayList<Hourly>,private  val activity:Activity)  : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>()  {
    var lang:String
    var unit:String
    var sharedPref: SharedPreferences
    lateinit var tempUnit:String
    init {
        sharedPref = activity.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang=sharedPref.getString("lang","en").toString()
        unit=sharedPref.getString("units","metric").toString()
        setUnits(unit)

    }
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
        if(lang.equals("en")) {
            holder.binding.dailyHour.text = timeFormat(hourlyList[position].dt.toInt())
            holder.binding.dailyTemb.text = (hourlyList[position].temp.toInt()).toString() + tempUnit
        }
        else{
            holder.binding.dailyHour.text = timeFormat(hourlyList[position].dt.toInt())
            holder.binding.dailyTemb.text = convertToArabic((hourlyList[position].temp.toInt()))+ tempUnit
        }
        Picasso.get().load(iconLinkgetter(hourlyList[position].weather.get(0).icon)).into(holder.binding.dailyImg)

        // holder.binding.dailyImg.setImageResource(hourlyList[position].)
    }
    private fun timeFormat(millisSeconds:Int ): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())
            val format = SimpleDateFormat("hh:mm aaa",Locale(lang.toString()))
            return format.format(calendar.time)

    }

    fun convertToArabic(value: Int): String? {
        return (value.toString() + "")
            .replace("1", "١").replace("2", "٢")
            .replace("3", "٣").replace("4", "٤")
            .replace("5", "٥").replace("6", "٦")
            .replace("7", "٧").replace("8", "٨")
            .replace("9", "٩").replace("0", "٠")
    }
    fun setUnits(unit:String)
    {
        when (unit) {
            "metric" -> {
                tempUnit="°c"
            }
            "imperial" -> {
                tempUnit = "°f"
            }
            "standard" ->{
                tempUnit="°k"
            }

        }
    }
    fun iconLinkgetter(iconName:String):String="https://openweathermap.org/img/wn/"+iconName+"@2x.png"
    inner class HourlyViewHolder constructor(val binding: HoursItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}