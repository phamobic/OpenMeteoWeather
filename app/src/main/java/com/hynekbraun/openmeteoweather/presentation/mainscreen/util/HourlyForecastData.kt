package com.hynekbraun.openmeteoweather.presentation.mainscreen.util

import com.hynekbraun.openmeteoweather.domain.util.WeatherType
import java.time.LocalDateTime

data class HourlyForecastData(
    val time: LocalDateTime,
    val temperature: Double,
    val weatherType: WeatherType,
)
