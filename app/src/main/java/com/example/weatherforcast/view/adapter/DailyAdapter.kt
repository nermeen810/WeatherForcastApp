package com.example.weatherforcast.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcast.databinding.DailyItemBinding
import com.example.weatherforcast.databinding.HoursItemBinding
import com.example.weatherforcast.model.Daily
import com.example.weatherforcast.model.Hourly
import com.squareup.picasso.Picasso
import java.util.*

class DailyAdapter (private val dailyList: ArrayList<Daily>)  : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>()  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): DailyAdapter.DailyViewHolder {
        return  DailyViewHolder(
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
        calendar.setTimeInMillis(dailyList[position].dt.toLong()*1000)
        var date=calendar.get(Calendar.DAY_OF_MONTH).toString()+"/"+(calendar.get(Calendar.MONTH)+1).toString()
        var day=calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).toString()
        var min=dailyList[position].temp.min.toInt()
        var max=dailyList[position].temp.max.toInt()
        
        holder.binding.day.text =day
        holder.binding.tempTxt.text=min.toString()+"°c : "+max+"°c"
        holder.binding.descriptionTxt.text=dailyList[position].weather[0].description
        holder.binding.dayMonthTxt.text=date
        Picasso.get().load(iconLinkgetter(dailyList[position].weather.get(0).icon)).into(holder.binding.imageView)
    }

    fun iconLinkgetter(iconName:String):String="https://openweathermap.org/img/wn/"+iconName+"@2x.png"
    fun updateDays(newDailyList: List<Daily>) {
        dailyList.clear()
        dailyList.addAll(newDailyList)
        notifyDataSetChanged()
    }
    inner class DailyViewHolder constructor(val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}