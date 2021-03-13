package com.example.weatherforcast.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.audiofx.BassBoost
import android.os.Looper
import android.widget.Toast
import android.provider.Settings;
import android.util.Log

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforcast.data.WeatherRepository
import com.example.weatherforcast.model.WeatherResponse
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*


class CurrentViewModel (application: Application) : AndroidViewModel(application) {
    var repository: WeatherRepository

    init {
    repository=WeatherRepository(application)

    }

     fun loadWeatherData(): LiveData<WeatherResponse> {

        return repository.fetchData()

    }



}