package com.hynekbraun.openmeteoweather.data.repository

import android.location.Location
import android.util.Log
import com.hynekbraun.openmeteoweather.data.local.WeatherDao
import com.hynekbraun.openmeteoweather.data.mapper.toWeatherData
import com.hynekbraun.openmeteoweather.data.mapper.toWeatherDataEntityList
import com.hynekbraun.openmeteoweather.data.network.WeatherApi
import com.hynekbraun.openmeteoweather.domain.WeatherData
import com.hynekbraun.openmeteoweather.domain.WeatherFetchError
import com.hynekbraun.openmeteoweather.domain.WeatherRepository
import com.hynekbraun.openmeteoweather.domain.util.Resource
import javax.inject.Inject


class WeatherRepositoryImp @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : WeatherRepository {
    override suspend fun getWeatherData(
        location: Location?
    ): Resource<WeatherData, WeatherFetchError> {
        Log.d("TAG", "Repository: Fetch data triggered")
        val isDbEmpty = dao.getWeather().isEmpty()
        if (location != null) {
            return try {
                val result = api.getWeatherData(lat = location.latitude, lon = location.longitude)
                Log.d(
                    "TAG",
                    "Repository: Fetched data: ${result.weatherData.time.size}"
                )
                dao.deleteWeather()
                result.weatherData.toWeatherDataEntityList().forEach {
                    dao.saveWeather(it)
                }
                Log.d("TAG", "Repository: Data loaded successfully")
                Resource.Success(data = dao.getWeather().toWeatherData())
            } catch (e: Exception) {
                Log.d("TAG", "Repository: Error fetching data ${e.message}")
                e.printStackTrace()
                if (isDbEmpty) Resource.Error(WeatherFetchError.NO_DATA)
                else Resource.Error(
                    error = WeatherFetchError.NETWORK_ERROR,
                    data = dao.getWeather().toWeatherData()
                )
            }
        } else if (!isDbEmpty) {
            return Resource.Success(
                data = dao.getWeather().toWeatherData()
            )
        } else {
            return Resource.Error(WeatherFetchError.NO_DATA)
        }
    }
}