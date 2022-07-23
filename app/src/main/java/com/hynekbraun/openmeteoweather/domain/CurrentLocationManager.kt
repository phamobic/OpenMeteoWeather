package com.hynekbraun.openmeteoweather.domain

import android.location.Location
import com.hynekbraun.openmeteoweather.domain.util.Resource

enum class LocationError{
    NO_PERMISSION, NO_GPS, GENERIC_ERROR
}

interface CurrentLocationManager {
    suspend fun getLocation(): Resource<Location, LocationError>
}