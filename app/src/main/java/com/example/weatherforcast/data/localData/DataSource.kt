package com.example.weatherforcast.data.localData

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherforcast.model.Alarm
import com.example.weatherforcast.model.WeatherResponse

class DataSource {
    var weatherDao: WeatherDao
     constructor(application: Application) {
        weatherDao = WeatherDataBase.getDatabase(application).weatherDao()
    }

    fun getAllWeather(): LiveData<List<WeatherResponse>> {
        return weatherDao.getAll()
    }

    fun getAllData(): List<WeatherResponse>{
        return weatherDao.getAllData()
    }
    fun getWeatherByLatLon(lat:String, lng:String): LiveData<WeatherResponse> {
        return weatherDao.getWeatherByLatLong(lat,lng)
    }
    fun getWeatherByTimezone(timezone:String?): LiveData<WeatherResponse> {
        return weatherDao.getWeatherByTimezone(timezone)
    }
    fun getByTimezone(timezone:String?): WeatherResponse{
        return weatherDao.getByTimezone(timezone)
    }
     fun deletByTimezone(timezone: String?) {
     weatherDao.deleteByTimeZone(timezone)

    }
    suspend fun insert(weather: WeatherResponse?) {
        weather?.let { weatherDao.insert(it) }
    }
     fun deleteAlarmObj(id: Int) {
        weatherDao.deleteAlarmObj(id)
    }

    fun getAllAlarmObj(): LiveData<List<Alarm>> {
        return weatherDao.getAllAlarms()
    }

    suspend fun insertAlarm(alarmObj: Alarm): Long {
        return weatherDao.insertAlarm(alarmObj)
    }
}