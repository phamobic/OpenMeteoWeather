package com.hynekbraun.openmeteoweather.presentation.mainscreen.util

import com.hynekbraun.openmeteoweather.domain.WeatherData

enum class ViewModelError{
    NO_LOCATION, NO_NETWORK, NO_DATA, NO_PERMISSION
}

data class WeatherState(
    val data: WeatherData? = null,
    val error: ViewModelError? = null,
    val isLoading: Boolean = false
)
