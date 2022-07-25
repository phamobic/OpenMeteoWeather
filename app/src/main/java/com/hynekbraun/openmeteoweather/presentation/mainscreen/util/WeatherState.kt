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
    val dailyForecastData: List<DailyForecastData> = emptyList(),
    val hourlyForecastData: List<HourlyForecastData> = emptyList(),
    val error: ViewModelError? = null,
    val isLoading: Boolean = true
)

