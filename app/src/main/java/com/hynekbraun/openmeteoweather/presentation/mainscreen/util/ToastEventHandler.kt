package com.hynekbraun.openmeteoweather.presentation.mainscreen.util

sealed class ToastEventHandler(val message: String) {

    data class NetworkToastEvent(val errorMessage: String = "Network Error") :
        ToastEventHandler(errorMessage)

    data class IOToastEvent(val errorMessage: String = "IO Error") :
        ToastEventHandler(errorMessage)

    data class HttpToastEvent(val errorMessage: String = "Http Error") :
        ToastEventHandler(errorMessage)

    data class NoConnectionToastEvent(val errorMessage: String = "No Connection") :
        ToastEventHandler(errorMessage)

    data class GpsEvent(val errorMessage: String = "Please turn on location"):
    ToastEventHandler(errorMessage)

    data class GenericToastEvent(val errorMessage: String = "Something went wrong") :
        ToastEventHandler(errorMessage)
}