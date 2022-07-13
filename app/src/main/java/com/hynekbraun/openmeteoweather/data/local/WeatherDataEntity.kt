package com.hynekbraun.openmeteoweather.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "windspeed_10m")
    val windSpeed: Double,
    @ColumnInfo(name = "temperature_2m")
    val temperature: Double,
    @ColumnInfo(name = "relativehumidity_2m")
    val humidity: Double
)
