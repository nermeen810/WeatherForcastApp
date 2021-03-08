package com.example.weatherforcast.data.remoteData

import com.example.weatherforcast.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
val appId ="6e86ea4f6d1336773d8addefb0f022a2"
interface WeatherApi {

    @GET("data/2.5/onecall")
    suspend fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String,@Query("exclude") exclude: String , @Query("units") units: String, @Query("lang") lang: String, @Query("appid") app_id: String= appId): Response<WeatherResponse>

}