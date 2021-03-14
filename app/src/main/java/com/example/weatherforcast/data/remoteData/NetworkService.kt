package com.example.weatherforcast.data.remoteData

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val BASE_URL = "https://api.openweathermap.org/"

    fun getCurrentData(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WeatherApi::class.java)
    }
}