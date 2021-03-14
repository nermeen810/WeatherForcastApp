package com.example.weatherforcast.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforcast.data.localData.DataSource
import com.example.weatherforcast.data.remoteData.NetworkService
import com.example.weatherforcast.model.WeatherResponse
import kotlinx.coroutines.*

class WeatherRepository {
    var localDataSource: DataSource
    var remoteDataSource: NetworkService
    var weatherLiveData = MutableLiveData<List<WeatherResponse>>()
    lateinit var latitude: String
    lateinit var longitude: String
    var lang: String
    var units: String
    var sharedPref: SharedPreferences

    constructor(application: Application) {
        localDataSource = DataSource(application)
        remoteDataSource = NetworkService
        sharedPref = application.getSharedPreferences("weather", Context.MODE_PRIVATE)
        lang = sharedPref.getString("lang", "en").toString()
        units = sharedPref.getString("units", "metric").toString()

    }

    fun fetchData(): LiveData<WeatherResponse> {
        latitude = sharedPref.getString("lat", "0").toString()
        longitude = sharedPref.getString("lng", "0").toString()
        if (latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) {
            val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
                Log.i("id", "exception")
            }
            CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
                val response = NetworkService.getCurrentData()
                    .getCurrentWeatherData(latitude, longitude, "minutely", units, lang)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        localDataSource.insert(response.body())
                        //  weatherLiveData.postValue(response.body())

                    }
                }
            }
        }
        return localDataSource.getWeatherByLatLon(latitude, longitude)
    }

    fun fetchWheatherData(timezone: String?): LiveData<WeatherResponse> {
        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {

            var weatherResponse = localDataSource.getWeatherByTimezone(timezone)
            latitude = weatherResponse.value?.lat.toString()
            longitude = weatherResponse.value?.lon.toString()
            withContext(Dispatchers.IO) {
                val response = NetworkService.getCurrentData()
                    .getCurrentWeatherData(latitude, longitude, "minutely", units, lang)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        localDataSource.insert(response.body())
                        //  weatherLiveData.postValue(response.body())

                    }
                }
            }

            // weatherLiveData.value=localDataSource.getWeatherByTimezone(timezone)
        }

        return localDataSource.getWeatherByTimezone(timezone)
    }

    fun getAllData(): LiveData<List<WeatherResponse>> {

        return localDataSource.getAllWeather()
    }

    fun setData(lat: String, lng: String) {

        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = NetworkService.getCurrentData()
                .getCurrentWeatherData(lat, lng, "minutely", units, lang)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    localDataSource.insert(response.body())
                    //  weatherLiveData.postValue(response.body())
                }
            }
        }
    }

    fun deleteData(timezone: String?) {
        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            localDataSource.deletByTimezone(timezone)

        }
    }

    fun refreshDataBase() {

        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Log.i("id", "exception")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            var list = localDataSource.getAllData()
            Log.i("key", "ooooooooooooooooooooooooooooooooooo" + list.toString())
            for (item in list!!) {
                setData(item.lat.toString(), item.lon.toString())
            }
        }


    }

}