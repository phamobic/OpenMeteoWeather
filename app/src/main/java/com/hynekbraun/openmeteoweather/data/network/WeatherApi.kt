package com.hynekbraun.openmeteoweather.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m,relativehumidity_2m,surface_pressure,weathercode,windspeed_10m")
    suspend fun getWeatherData(
        @Query("latitude")lat: Double,
        @Query("longitude")lon: Double,
    ): WeatherDto
}