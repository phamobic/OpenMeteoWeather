package com.hynekbraun.openmeteoweather.presentation.mainscreen.util

import com.hynekbraun.openmeteoweather.domain.WeatherData
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerDay
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerHour
import java.time.LocalDateTime

enum class ViewModelError{
    NO_LOCATION, NO_NETWORK, NO_DATA, NO_PERMISSION
}

data class WeatherState(
    val currentData: CurrentData? = null,
    val forecast: List<WeatherDataPerDay> = emptyList(),
    val error: ViewModelError? = null,
    val isLoading: Boolean = false
)

data class CurrentData(
    val currentTime: LocalDateTime,
    val weatherData: WeatherDataPerHour?
)