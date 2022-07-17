package com.hynekbraun.openmeteoweather.presentation.mainscreen.util

sealed class WeatherEvent{
    object FetchData: WeatherEvent()
}
