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
            val currentLocation = currentLocation.getLocation()
            Log.d("TAG", "WeatherViewModel: Location fetched: $currentLocation")
            when (currentLocation) {
                is Resource.Error -> {
                    fetchWeather(null)
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

    private fun fetchWeather(location: Location?) {
        viewModelScope.launch {
            repository.getWeatherData(location)
                .let { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { weatherData ->
                                weatherState = weatherState.copy(
                                    currentData = weatherData.toCurrentData(),
                                    hourlyForecastData = weatherData.toCurrentHourlyForecastData(),
                                    dailyForecastData = weatherData
                                        .weatherData
                                        .map { daily ->
                                            daily
                                                .toDailyForecastData()
                                        }, isLoading = false
                                )
                            }
                        }
                        is Resource.Error -> {
                            weatherState = weatherState.copy(isLoading = false)
                            when (resource.error) {
                                WeatherFetchError.NO_DATA -> eventChannel.send(
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