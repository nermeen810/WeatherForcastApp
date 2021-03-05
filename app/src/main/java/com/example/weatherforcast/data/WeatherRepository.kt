package com.example.weatherforcast.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforcast.data.localData.DataSource
import com.example.weatherforcast.data.remoteData.NetworkService
import com.example.weatherforcast.model.WeatherResponse
import kotlinx.coroutines.*

class WeatherRepository {
    var localDataSource: DataSource
    var remoteDataSource:NetworkService
    var weatherLiveData= MutableLiveData<WeatherResponse>()

    constructor(application: Application){
        localDataSource = DataSource(application)
        remoteDataSource=NetworkService
    }
    fun fetchData(lat:String,lon:String) :LiveData<List<WeatherResponse>>{


        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace()
            Log.i("id","exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = NetworkService.getCurrentData().getCurrentWeatherData(lat,lon)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    localDataSource.insert(response.body())
                    weatherLiveData.postValue(response.body())

                }
            }
        }
        return localDataSource.getWeather()
    }

    fun getAllData(lat:String,lon:String) :LiveData<WeatherResponse>{
        var weatherLiveData= MutableLiveData<WeatherResponse>()

        val exceptionHandlerException = CoroutineExceptionHandler { _, _ ->
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = NetworkService.getCurrentData().getCurrentWeatherData(lat,lon)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    localDataSource.insert(response.body())
                    weatherLiveData.postValue(response.body())

                }
            }
        }
        return weatherLiveData
    }
}