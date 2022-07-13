package com.hynekbraun.openmeteoweather.domain

import com.hynekbraun.openmeteoweather.domain.util.Resource

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherDataPerDay>
}