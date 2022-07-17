package com.hynekbraun.openmeteoweather.domain

import com.hynekbraun.openmeteoweather.domain.util.Resource

enum class WeatherError {
    EMPTY_DB, NETWORK_ERROR
}

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherData, WeatherError>
}