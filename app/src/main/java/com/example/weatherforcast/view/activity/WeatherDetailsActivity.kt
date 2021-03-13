package com.example.weatherforcast.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.ActivityWeatherDetailsBinding
import com.example.weatherforcast.databinding.FragmentCurrentBinding
import com.example.weatherforcast.databinding.FragmentFavoriteBinding
import com.example.weatherforcast.model.WeatherResponse
import com.example.weatherforcast.view.adapter.DailyAdapter
import com.example.weatherforcast.view.adapter.FavoriteAdapter
import com.example.weatherforcast.view.adapter.HourlyAdapter
import com.example.weatherforcast.viewModel.FavoriteViewModel
import com.example.weatherforcast.viewModel.WeatherDetailsViewModel
import com.squareup.picasso.Picasso
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailsActivity : AppCompatActivity() {
    lateinit var weatherDetailsViewModel: WeatherDetailsViewModel
    lateinit var binding: ActivityWeatherDetailsBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var  dailyAdapter: DailyAdapter
    lateinit var sharedPref: SharedPreferences
    lateinit var lang:String
    lateinit var unit:String
    lateinit var tempUnit:String
    lateinit var windSpeedUnit:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val timezone= intent.getStringExtra("timezone")
        sharedPref =getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang=sharedPref.getString("lang","en").toString()
        unit=sharedPref.getString("units","metric").toString()
        setUnits(unit)
        hourlyAdapter = HourlyAdapter(arrayListOf(),this)
        dailyAdapter = DailyAdapter(arrayListOf(),this)
        initUI()

        weatherDetailsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(
            WeatherDetailsViewModel::class.java
        )
         weatherDetailsViewModel.loadWeatherData(timezone).observe(this, {
            it?.let {
                setData(it)
            }
        })
    }

    private fun initUI() {
        binding.hourlyRecycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        binding.dailyRecycle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = dailyAdapter
        }
    }

    private fun setData(it: WeatherResponse) {
        hourlyAdapter.updateHours(it.hourly)
        dailyAdapter.updateDays(it.daily)
       // Picasso.get().load(iconLinkgetter(it.current.weather[0].icon)).into(binding.MainIcon)
        binding.mainDescription.text = it.current.weather[0].description
        binding.mainViewDate.text = dateFormat(it.current.dt)
        binding.SunriseVal.text = timeFormat(it.current.sunrise)
        binding.SunsetVal.text = timeFormat(it.current.sunset)
        binding.mainViewCountry.text = convertTimezone(it)
        if(lang.equals("en")) {
            binding.HumidityVal.text = it.current.humidity.toString()+ "%"
            binding.PressureVal.text = it.current.pressure.toString()+ "hPa"
            binding.WindSpeedVal.text = it.current.wind_speed.toString() + windSpeedUnit
            binding.mainTemperature.text = (it.current.temp.toInt()).toString() + tempUnit
            binding.CloudsVal.text = it.current.clouds.toString()

        }
        else
        {
            binding.CloudsVal.text = convertToArabic(it.current.clouds)
            binding.HumidityVal.text = convertToArabic(it.current.humidity) + "%"
            binding.PressureVal.text = convertToArabic(it.current.pressure) +"hPa"
            binding.WindSpeedVal.text = convertToArabic(it.current.wind_speed.toInt())+windSpeedUnit
            binding.mainTemperature.text = convertToArabic(it.current.temp.toInt()) +tempUnit


        }

    }

    private fun timeFormat(millisSeconds: Int): String {
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis((millisSeconds * 1000).toLong())

            val format = SimpleDateFormat("hh:mm aaa",Locale(lang))
            return format.format(calendar.time)

    }

    private fun dateFormat(milliSeconds: Int): String {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var year = calendar.get(Calendar.YEAR)
        if(lang.equals("en")) {
            return day.toString() + "/" + month + "/" + year
        }
        else
        {
            return convertToArabic(year)+"/"+convertToArabic(month)+"/"+convertToArabic(day)
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
    fun convertTimezone(weatherResponse: WeatherResponse):String{
        var arabicTimezone=""
        var addressList: List<Address>? = null

        val geocoder= Geocoder(this, Locale(lang))
        try {
            addressList = geocoder.getFromLocation(weatherResponse.lat,weatherResponse.lon, 1)
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }

        val address = addressList!![0]
        arabicTimezone=address.locality+"/"+address.adminArea
        return arabicTimezone
    }
    fun setUnits(unit:String)
    {
        when (unit) {
            "metric" -> {
                tempUnit="°c"
                windSpeedUnit="m/s"
            }
            "imperial" -> {
                tempUnit = "°f"
                windSpeedUnit="m/h"
            }
            "standard" ->{
                tempUnit="°k"
                windSpeedUnit="m/s"
            }

        }
    }
    fun iconLinkgetter(iconName:String):String="https://openweathermap.org/img/wn/"+iconName+"@2x.png"

}