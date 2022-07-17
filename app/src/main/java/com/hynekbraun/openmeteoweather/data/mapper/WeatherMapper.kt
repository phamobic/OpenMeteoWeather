package com.hynekbraun.openmeteoweather.data.mapper

import com.hynekbraun.openmeteoweather.data.local.WeatherDataEntity
import com.hynekbraun.openmeteoweather.data.network.WeatherDataDto
import com.hynekbraun.openmeteoweather.domain.WeatherData
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerDay
import com.hynekbraun.openmeteoweather.domain.WeatherDataPerHour
import com.hynekbraun.openmeteoweather.domain.util.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun WeatherDataDto.toWeatherDataEntityList(): List<WeatherDataEntity> {
    return time.mapIndexed { index, time ->
        val temperature = temperature[index]
        val windSpeed = windSpeed[index]
        val humidity = humidity[index]
        val code = code[index]
        val pressure = pressure[index]
        WeatherDataEntity(
            temperature = temperature,
            windSpeed = windSpeed,
            humidity = humidity,
            weatherCode = code,
            time = time,
            pressure = pressure
        )
    }
}

fun WeatherDataEntity.toWeatherDataPerHour(): WeatherDataPerHour{
    return WeatherDataPerHour(
        time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
        temperature = temperature,
        windSpeed = windSpeed,
        humidity = humidity,
        weatherType = WeatherType.fromWMO(weatherCode),
        pressure = pressure
    )
}

fun List<WeatherDataEntity>.toWeatherData(): WeatherData {

    val daysChunked = this.chunked(7){ listPerDay ->
         WeatherDataPerDay(
           day =  LocalDateTime.parse(listPerDay.first().time, DateTimeFormatter.ISO_DATE_TIME),
            hourlyWeather = listPerDay.map { it.toWeatherDataPerHour() }
        )
    }
    return WeatherData(
        weatherData = daysChunked
    )
}

