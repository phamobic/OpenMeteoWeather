package com.hynekbraun.openmeteoweather.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class WeatherDataPerHour(
    val time: String,
    val windSpeed: Double,
    val temperature: Double,
    val humidity: Double,
    val weatherCode: Int
)
