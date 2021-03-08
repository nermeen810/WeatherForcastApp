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
    var weatherLiveData= MutableLiveData<List<WeatherResponse>>()
    var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application.applicationContext)
    var latitude = 33.4418
    var longitude = -94.0377
    var address: String? = null
    val PERMISSION_ID = 20
    init {
        repository= WeatherRepository(application)
    }

    fun loadWeatherData(): LiveData<WeatherResponse> {

        return repository.fetchData(latitude.toString(),longitude.toString())

    }
}