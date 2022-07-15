package com.hynekbraun.openmeteoweather.domain

import com.hynekbraun.openmeteoweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherData>
}