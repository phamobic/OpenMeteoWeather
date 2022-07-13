package com.hynekbraun.openmeteoweather.domain

import java.time.LocalDateTime

data class WeatherDataPerDay(
    val date: LocalDateTime,
    val currentTime: LocalDateTime?,
    val dailyWeather: List<WeatherDataPerHour>,
)
