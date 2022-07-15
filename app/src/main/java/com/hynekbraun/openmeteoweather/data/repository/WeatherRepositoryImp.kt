package com.hynekbraun.openmeteoweather.data.repository

import android.util.Log
import com.hynekbraun.openmeteoweather.data.local.WeatherDao
import com.hynekbraun.openmeteoweather.data.mapper.toWeatherData
import com.hynekbraun.openmeteoweather.data.mapper.toWeatherDataEntityList
import com.hynekbraun.openmeteoweather.data.network.WeatherApi
import com.hynekbraun.openmeteoweather.domain.WeatherData
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerDay
import com.hynekbraun.openmeteoweather.domain.WeatherRepository
import com.hynekbraun.openmeteoweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : WeatherRepository {
    override suspend fun getWeatherData(
        lat: Double,
        lon: Double
    ): Resource<WeatherData> {
        val isDbEmpty = dao.getWeather().isEmpty()
        return try {
            val result = api.getWeatherData(lat = lat, lon = lon)
            dao.deleteWeather()
            dao.saveWeather(result.weatherData.toWeatherDataEntityList())
            Log.d("TAG", "Repository: Data loaded successfully")
            Resource.Success(data = dao.getWeather().toWeatherData())
        } catch (e: Exception) {
            Log.d("TAG", "Repository: Error fetching data")
            if (isDbEmpty) Resource.Error(e.message ?: "Unknown error")
            else Resource.Error(
                message = "Could not load data from network",
                data = dao.getWeather().toWeatherData()
            )
        }
    }
}