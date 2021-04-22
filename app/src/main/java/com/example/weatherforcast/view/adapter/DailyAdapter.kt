package com.example.weatherforcast.view.adapter

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.databinding.DailyItemBinding
import com.example.weatherforcast.databinding.HoursItemBinding
import com.example.weatherforcast.model.Daily
import com.example.weatherforcast.model.Hourly
import com.squareup.picasso.Picasso
import java.util.*

class DailyAdapter(private val dailyList: ArrayList<Daily>, private val activity: Activity) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    var lang: String
    var unit: String
    var sharedPref: SharedPreferences
    lateinit var tempUnit: String

    init {
        sharedPref = activity.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang = sharedPref.getString("lang", "en").toString()
        unit = sharedPref.getString("units", "metric").toString()
        setUnits(unit)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): DailyAdapter.DailyViewHolder {
        return DailyViewHolder(
            DailyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

    override fun onBindViewHolder(holder: DailyAdapter.DailyViewHolder, position: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(dailyList[position].dt.toLong() * 1000)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = (calendar.get(Calendar.MONTH) + 1)
        var dayName =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale(lang)).toString()
        var min = dailyList[position].temp.min.toInt()
        var max = dailyList[position].temp.max.toInt()
        holder.binding.descriptionTxt.text = dailyList[position].weather[0].description
        holder.binding.day.text = dayName


        if (lang.equals("en")) {
            holder.binding.tempTxt.text = min.toString() + tempUnit + " : " + max + tempUnit
            holder.binding.dayMonthTxt.text = day.toString() + "/" + month.toString()
        } else {
            holder.binding.tempTxt.text =
                convertToArabic(max) + tempUnit + " : " + convertToArabic(min) + tempUnit
            holder.binding.dayMonthTxt.text = convertToArabic(month) + "/" + convertToArabic(day)
        }
    }

    fun convertToArabic(value: Int): String? {
        return (value.toString() + "")
            .replace("1", "١").replace("2", "٢")
            .replace("3", "٣").replace("4", "٤")
            .replace("5", "٥").replace("6", "٦")
            .replace("7", "٧").replace("8", "٨")
            .replace("9", "٩").replace("0", "٠")
    }

    fun setUnits(unit: String) {
        when (unit) {
            "metric" -> {
                tempUnit = "°c"
            }
            "imperial" -> {
                tempUnit = "°f"
            }
            "standard" -> {
                tempUnit = "°k"
            }

        }
    }

    fun iconLinkgetter(iconName: String): String =
        "https://openweathermap.org/img/wn/" + iconName + "@2x.png"

    fun updateDays(newDailyList: List<Daily>) {
        dailyList.clear()
        dailyList.addAll(newDailyList)
        notifyDataSetChanged()
    }

    inner class DailyViewHolder constructor(val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}