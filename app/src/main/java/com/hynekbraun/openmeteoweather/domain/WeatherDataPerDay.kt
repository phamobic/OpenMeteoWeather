package com.hynekbraun.openmeteoweather.domain

import java.time.LocalDateTime

data class WeatherDataPerDay(
    val day: LocalDateTime,
    val hourlyWeather: List<WeatherDataPerHour>,
)
