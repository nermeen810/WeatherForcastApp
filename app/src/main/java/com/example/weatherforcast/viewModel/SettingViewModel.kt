package com.example.weatherforcast.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse

class SettingViewModel  (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository
    init {
        repository= WeatherRepository(application)
    }

    fun refreshData(){
      repository.refreshDataBase()
    }
}