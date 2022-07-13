package com.hynekbraun.openmeteoweather.data.repository

import com.hynekbraun.openmeteoweather.data.local.WeatherDao
import com.hynekbraun.openmeteoweather.data.network.WeatherApi
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerDay
import com.hynekbraun.openmeteoweather.domain.WeatherRepository
import com.hynekbraun.openmeteoweather.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : WeatherRepository {
    override suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherDataPerDay> {
        return Resource.Error("")
    }
}