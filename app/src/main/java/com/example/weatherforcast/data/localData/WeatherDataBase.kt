package com.example.weatherforcast.data.localData

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforcast.model.Alarm
import com.example.weatherforcast.model.WeatherResponse

@Database(
    entities = arrayOf(WeatherResponse::class, Alarm::class),
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
public abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getDatabase(application: Application): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    WeatherDataBase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}