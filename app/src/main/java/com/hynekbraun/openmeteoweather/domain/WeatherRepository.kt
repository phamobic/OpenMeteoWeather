package com.hynekbraun.openmeteoweather.domain

import android.location.Location
import com.hynekbraun.openmeteoweather.domain.util.Resource

enum class WeatherFetchError {
    NO_DATA, NETWORK_ERROR
}

interface WeatherRepository {

    suspend fun getWeatherData(location: Location?): Resource<WeatherData, WeatherFetchError>
}