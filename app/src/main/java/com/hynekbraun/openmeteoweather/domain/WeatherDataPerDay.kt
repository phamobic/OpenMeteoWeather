package com.hynekbraun.openmeteoweather.domain

import java.time.LocalDateTime

data class WeatherDataPerDay(
    val dayOfMonth: LocalDateTime,
    val dailyWeather: List<WeatherDataPerHour>,
)
