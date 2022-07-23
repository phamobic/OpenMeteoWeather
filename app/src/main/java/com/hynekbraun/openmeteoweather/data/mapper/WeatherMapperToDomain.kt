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
    val result: MutableList<WeatherDataEntity> = mutableListOf()

    for (i in 0 until this.temperature.size){
        result.add(
            WeatherDataEntity(
                time = this.time[i],
                windSpeed = this.windSpeed[i],
                humidity = this.humidity[i],
                pressure = this.pressure[i],
                temperature = this.temperature[i],
                weatherCode = this.code[i]
            )
        )
    }
    return result.toList()
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

    val daysChunked = this.chunked(21){ listPerDay ->
         WeatherDataPerDay(
           day =  LocalDateTime.parse(listPerDay.first().time, DateTimeFormatter.ISO_DATE_TIME),
            hourlyWeather = listPerDay.map { it.toWeatherDataPerHour() }
        )
    }
    return WeatherData(
        weatherData = daysChunked
    )
}

