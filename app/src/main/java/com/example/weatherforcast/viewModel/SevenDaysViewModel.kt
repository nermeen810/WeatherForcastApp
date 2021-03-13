package com.example.weatherforcast.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SevenDaysViewModel  (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository
    init {
        repository= WeatherRepository(application)
    }

    fun loadWeatherData(): LiveData<WeatherResponse> {

        return repository.fetchData()

    }
}