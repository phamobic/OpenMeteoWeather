package com.hynekbraun.openmeteoweather.presentation.mainscreen.util

import com.hynekbraun.openmeteoweather.domain.WeatherDataPerHour
import java.time.LocalDateTime

data class CurrentData(
    val currentTime: LocalDateTime,
    val weatherData: WeatherDataPerHour?
)