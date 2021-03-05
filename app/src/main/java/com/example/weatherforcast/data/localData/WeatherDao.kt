package com.example.weatherforcast.data.localData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforcast.model.WeatherResponse

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather")
    fun getAll(): LiveData<List<WeatherResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherResponse)

    @Query("DELETE FROM Weather")
    suspend fun deleteAll()

}