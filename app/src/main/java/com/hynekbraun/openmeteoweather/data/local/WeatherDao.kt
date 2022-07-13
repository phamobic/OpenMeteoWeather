package com.hynekbraun.openmeteoweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveWeather(Weather: List<WeatherDataEntity>)

    @Query("SELECT * FROM weather_table")
    fun getWeather(): Flow<List<WeatherDataEntity>>
}