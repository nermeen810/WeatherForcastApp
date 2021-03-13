package com.example.weatherforcast.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse
import java.util.*

class WeatherDetailsViewModel (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository

    init {
        repository = WeatherRepository(application)

    }

    fun loadWeatherData(timeZone: String?): LiveData<WeatherResponse> {

        return repository.fetchWheatherData(timeZone)

    }
}