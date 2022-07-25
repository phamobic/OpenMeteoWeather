package com.hynekbraun.openmeteoweather.domain.mapper

import com.hynekbraun.openmeteoweather.domain.WeatherData
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerDay
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerHour
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.CurrentData
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.DailyForecastData
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.HourlyForecastData
import java.time.LocalDateTime

fun WeatherData.toCurrentData(): CurrentData {
    val currentTime = LocalDateTime.now()
    return CurrentData(
        weatherData = this.weatherData[0].hourlyWeather.find {
            it.time.hour == currentTime.hour
        },
    )
}

fun WeatherData.toCurrentHourlyForecastData(): List<HourlyForecastData> {
    val dataPerHour = this.weatherData[0].hourlyWeather
    val finalList: MutableList<HourlyForecastData> = mutableListOf<HourlyForecastData>()
    dataPerHour.forEach {
        finalList.add(
            HourlyForecastData(
                time = it.time,
                temperature = it.temperature,
                weatherType = it.weatherType
            )
        )
    }
    return finalList
}

fun WeatherDataPerHour.toHourlyForecastData(): HourlyForecastData {
    return HourlyForecastData(
        temperature = this.temperature,
        time = this.time,
        weatherType = this.weatherType
    )
}

fun WeatherDataPerDay.toDailyForecastData(): DailyForecastData {
    val averageTemp = this.hourlyWeather.sumOf { it.temperature }.div(24)
    return DailyForecastData(
        time = this.day,
        temperature = averageTemp,
        weatherType = this.hourlyWeather[0].weatherType
    )
}