package com.example.weatherforcast.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse

class FavoriteViewModel  (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository

    init {
        repository = WeatherRepository(application)
    }

    fun loadWeatherData(): LiveData<List<WeatherResponse>> {

        return repository.getAllData()

    }
    fun deletWeatherData(timezone:String){

         repository.deleteData(timezone)

    }
}