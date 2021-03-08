package com.example.weatherforcast.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforcast.model.WeatherResponse

class DataSource {
    var weatherDao: WeatherDao
     constructor(application: Application) {
        weatherDao = WeatherDataBase.getDatabase(application).weatherDao()
    }

    fun getAllWeather(): LiveData<List<WeatherResponse>> {
        return weatherDao.getAll()
    }

    fun getWeather(lat:String,lng:String): LiveData<WeatherResponse> {
        return weatherDao.getWeatherResponse(lat,lng)
    }
    suspend fun insert(weather: WeatherResponse?) {
        weather?.let { weatherDao.insert(it) }
    }
}