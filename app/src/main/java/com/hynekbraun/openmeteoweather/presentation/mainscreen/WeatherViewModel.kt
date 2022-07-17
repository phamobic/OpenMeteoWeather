package com.hynekbraun.openmeteoweather.presentation.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hynekbraun.openmeteoweather.domain.CurrentLocationManager
import com.hynekbraun.openmeteoweather.domain.LocationError
import com.hynekbraun.openmeteoweather.domain.WeatherFetchError
import com.hynekbraun.openmeteoweather.domain.WeatherRepository
import com.hynekbraun.openmeteoweather.domain.util.Resource
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.ToastEventHandler
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.ViewModelError
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.WeatherEvent
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.WeatherState
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
        viewModelScope.launch {
            weatherState = weatherState.copy(
                isLoading = true, error = null
            )
            currentLocation.getLocation().let { locationCall ->
                when (locationCall) {
                    is Resource.Success -> {
                        locationCall.data?.let {
                            when (val weather =
                                repository.getWeatherData(it.latitude, it.longitude)
                            ) {
                                is Resource.Success -> {
                                    weatherState = weatherState.copy(
                                        data = weather.data,
                                        isLoading = false,
                                        error = null
                                    )
                                }
                                is Resource.Error -> {
                                    when (weather.error) {
                                        WeatherFetchError.EMPTY_DB -> weatherState =
                                            weatherState.copy(
                                                data = null,
                                                error = ViewModelError.NO_DATA,
                                                isLoading = false
                                            )
                                        WeatherFetchError.NETWORK_ERROR -> {
                                            weatherState = weatherState.copy(
                                                data = weather.data,
                                                error = ViewModelError.NO_NETWORK,
                                                isLoading = false
                                            )
                                            eventChannel.send(ToastEventHandler.NetworkToastEvent())
                                        }
                                        null -> {}
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        when (locationCall.error) {
                            LocationError.NO_PERMISSION -> weatherState = weatherState.copy(
                                error = ViewModelError.NO_PERMISSION,
                                isLoading = false
                            )
                            LocationError.NO_GPS ->
                                eventChannel.send(ToastEventHandler.GpsEvent())
                            LocationError.GENERIC_ERROR ->
                                eventChannel.send(ToastEventHandler.GenericToastEvent())
                            null -> {}
                        }
                    }
                }
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            weatherState = weatherState.copy(isLoading = true, error = null)
            weatherState = weatherState.copy(
                data = repository.observeDatabase(),
                isLoading = false
            )
        }
    }
}