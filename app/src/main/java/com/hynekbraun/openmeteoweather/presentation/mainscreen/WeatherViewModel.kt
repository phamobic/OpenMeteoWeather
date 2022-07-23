package com.hynekbraun.openmeteoweather.presentation.mainscreen

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hynekbraun.openmeteoweather.domain.CurrentLocationManager
import com.hynekbraun.openmeteoweather.domain.LocationError
import com.hynekbraun.openmeteoweather.domain.WeatherFetchError
import com.hynekbraun.openmeteoweather.domain.WeatherRepository
import com.hynekbraun.openmeteoweather.domain.mapper.toCurrentData
import com.hynekbraun.openmeteoweather.domain.mapper.toCurrentHourlyForecastData
import com.hynekbraun.openmeteoweather.domain.mapper.toDailyForecastData
import com.hynekbraun.openmeteoweather.domain.mapper.toHourlyForecastData
import com.hynekbraun.openmeteoweather.domain.util.Resource
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val currentLocation: CurrentLocationManager
) : ViewModel() {

    init {
        observeData()
    }

    var weatherState by mutableStateOf(WeatherState())
        private set

    private val eventChannel = Channel<ToastEventHandler>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun onEvent(event: WeatherEvent) {
        when (event) {
            WeatherEvent.FetchData -> fetchData()
        }
    }


    private fun fetchData() {
        Log.d("TAG", "WeatherViewModel: fetch data triggered")
        viewModelScope.launch {
            weatherState = weatherState.copy(
                isLoading = true, error = null
            )
            val currentLocation = currentLocation.getLocation()
            Log.d("TAG", "WeatherViewModel: Location fetched: $currentLocation")
            when (currentLocation) {
                is Resource.Error -> {
                    when (currentLocation.error) {
                        LocationError.NO_PERMISSION -> {
                            eventChannel.send(ToastEventHandler.PermissionEvent())
                        }
                        LocationError.NO_GPS -> {
                            eventChannel.send(ToastEventHandler.GpsEvent())
                        }
                        LocationError.GENERIC_ERROR -> {
                            eventChannel.send(ToastEventHandler.GenericToastEvent())
                        }
                        null -> {
                            eventChannel.send(ToastEventHandler.GenericToastEvent())
                        }
                    }
                }
                is Resource.Success -> fetchWeather(currentLocation.data)
            }

        }
    }

    private fun observeData() {
        Log.d("TAG", "WeatherViewModel: observe database triggered")
        viewModelScope.launch {
            try {
            val weather = repository.observeDatabase()
            Log.d("TAG", "WeatherViewModel: observe data current date ${weather.toCurrentData()}")
            Log.d("TAG", "WeatherViewModel: observe data currentHoulry ${weather.toCurrentHourlyForecastData().size}")
            weatherState = weatherState.copy(isLoading = true, error = null)
            weatherState = weatherState.copy(
                currentData = weather.toCurrentData(),
                hourlyForecastData = weather.toCurrentHourlyForecastData(),
                dailyForecastData = weather
                    .weatherData
                    .map { daily ->
                        daily
                            .toDailyForecastData()
                    }, isLoading = false
            )
                Log.d("TAG", "ViewModel: daily forecast: ${weatherState.dailyForecastData[0]}")
            } catch (e: Exception){
                e.printStackTrace()

            }
        }
    }

    private fun fetchWeather(location: Location?) {
        if (location != null) {
            viewModelScope.launch {
                repository.getWeatherData(lat = location.latitude, lon = location.longitude)
                    .let { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                resource.data?.let { weatherData ->
                                    weatherState = weatherState.copy(
                                        currentData = weatherData.toCurrentData(),
                                        hourlyForecastData = weatherData.weatherData[0]
                                            .hourlyWeather
                                            .map { hourly ->
                                                hourly.toHourlyForecastData()
                                            }
                                    )
                                }
                            }
                            is Resource.Error -> {
                                when (resource.error) {
                                    WeatherFetchError.EMPTY_DB -> eventChannel.send(
                                        ToastEventHandler.GenericToastEvent()
                                    )
                                    WeatherFetchError.NETWORK_ERROR -> eventChannel.send(
                                        ToastEventHandler.NetworkToastEvent()
                                    )
                                    null -> eventChannel.send(ToastEventHandler.GenericToastEvent())
                                }
                            }
                        }
                    }
            }
        }
    }
}