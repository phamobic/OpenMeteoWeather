package com.hynekbraun.openmeteoweather.data.network

import com.squareup.moshi.Json

data class WeatherDataDto(
    @field:Json(name = "time")
    val time: List<String>,
    @field:Json(name = "windspeed_10m")
    val windSpeed: List<Double>,
    @field:Json(name = "temperature_2m")
    val temperature: List<Double>,
    @field:Json(name = "relativehumidity_2m")
    val humidity: List<Double>
)
