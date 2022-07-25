package com.hynekbraun.openmeteoweather.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.hynekbraun.openmeteoweather.domain.util.WeatherType
import java.time.LocalDateTime

data class WeatherDataPerHour(
    val time: LocalDateTime,
    val windSpeed: Double,
    val temperature: Double,
    val humidity: Double,
    val weatherType: WeatherType,
    val pressure: Double
)
