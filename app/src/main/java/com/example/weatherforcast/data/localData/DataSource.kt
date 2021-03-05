package com.example.weatherforcast.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforcast.model.WeatherResponse

class DataSource {
    var weatherDao: WeatherDao
     constructor(application: Application) {
        weatherDao = WeatherDataBase.getDatabase(application).weatherDao()
    }

    fun getWeather(): LiveData<List<WeatherResponse>> {
        return weatherDao.getAll()
    }

    suspend fun insert(weather: WeatherResponse?) {
        weather?.let { weatherDao.insert(it) }
    }
}