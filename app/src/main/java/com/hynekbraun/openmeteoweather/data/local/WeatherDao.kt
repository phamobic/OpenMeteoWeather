package com.hynekbraun.openmeteoweather.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveWeather(Weather: WeatherDataEntity)

    @Query("SELECT * FROM weather_table")
    suspend fun getWeather(): List<WeatherDataEntity>

    @Query("DELETE FROM weather_table")
    suspend fun deleteWeather()
}