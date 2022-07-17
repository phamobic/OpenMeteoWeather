package com.hynekbraun.openmeteoweather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.hynekbraun.openmeteoweather.domain.LocationError
import com.hynekbraun.openmeteoweather.domain.CurrentLocationManager
import com.hynekbraun.openmeteoweather.domain.util.Resource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class CurrentLocationManagerImp
@Inject constructor(
    val application: Application,
    val locationClient: FusedLocationProviderClient
) : CurrentLocationManager {
    override suspend fun getLocation(): Resource<Location?, LocationError> {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        val isGpsEnabled =
            locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)

        if (!hasAccessFineLocationPermission || !hasAccessCoarseLocationPermission)
            return Resource.Error(error = LocationError.NO_PERMISSION)
        else if (!isGpsEnabled) return Resource.Error(error = LocationError.NO_GPS)

        //this block coroutine where we use it and wait for the result
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(Resource.Success(data = result))
                    } else {
                        cont.resume(Resource.Error(error = LocationError.GENERIC_ERROR))
                    }
                    return@suspendCancellableCoroutine
                }

                addOnSuccessListener {
                    cont.resume(Resource.Success(data = result))
                }
                addOnFailureListener {
                    cont.resume(Resource.Error(error = LocationError.GENERIC_ERROR))
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }

        }

    }
}

