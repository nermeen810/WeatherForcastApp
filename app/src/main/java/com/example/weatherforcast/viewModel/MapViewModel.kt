package com.example.weatherforcast.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse

class MapViewModel  (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository
    init {
        repository= WeatherRepository(application)
    }

    fun setWeatherData(lat:String,lng:String){

        return repository.setData(lat,lng)

    }
}