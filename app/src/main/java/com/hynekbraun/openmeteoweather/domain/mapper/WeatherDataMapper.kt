package com.hynekbraun.openmeteoweather.domain.mapper

import com.hynekbraun.openmeteoweather.domain.WeatherData
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerDay
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.CurrentData
import java.time.LocalDateTime

fun WeatherData.toCurrentData(): CurrentData{
    val currentTime = LocalDateTime.now()
    return CurrentData(
        currentTime = currentTime,
        weatherData = this.weatherData[0].hourlyWeather.find {
            it.time.hour == currentTime.hour},
    )


}