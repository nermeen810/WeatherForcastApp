package com.example.weatherforcast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforcast.model.Alarm
import com.example.weatherforcast.model.WeatherResponse

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather")
    fun getAll(): LiveData<List<WeatherResponse>>

    @Query("SELECT * FROM Weather")
    fun getAllData(): List<WeatherResponse>

    @Query("SELECT * FROM Weather WHERE lat=:lat AND lon=:lng ")
    fun getWeatherByLatLong(lat: String, lng: String): LiveData<WeatherResponse>

    @Query("SELECT * FROM Weather WHERE timezone=:timezone")
    fun getWeatherByTimezone(timezone: String?): LiveData<WeatherResponse>

    @Query("SELECT * FROM Weather WHERE timezone=:timezone")
    fun getByTimezone(timezone: String?): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherResponse)

    @Query("DELETE FROM Weather")
    suspend fun deleteAll()

    @Query("DELETE FROM Weather WHERE timezone=:timezone")
    fun deleteByTimeZone(timezone: String?)

    @Query("SELECT * FROM Alarms")
    fun getAllAlarms(): LiveData<List<Alarm>>

    @Query("SELECT * FROM Alarms Where id =:id ")
    fun getApiObj(id: Int): Alarm

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmObj: Alarm): Long

    @Query("DELETE FROM Alarms WHERE id =:id")
    fun deleteAlarmObj(id: Int): Unit
}