package com.hynekbraun.openmeteoweather.domain.util

import android.location.Location

//sealed class Resource<T>(val data: T? = null, val message: String? = null) {
//    class Success<T>(data: T?) : Resource<T>(data)
//    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
//}
sealed class Resource<S, E>(val data: S? = null, val error: E? = null) {
    class Success<S, Nothing>(data: S) : Resource<S, Nothing>(data)
    class Error<S, E>(error: E, data: S? = null) : Resource<S, E>(error = error, data = data)
}