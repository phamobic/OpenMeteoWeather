package com.hynekbraun.openmeteoweather

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hynekbraun.openmeteoweather.presentation.mainscreen.*
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.WeatherEvent
import com.hynekbraun.openmeteoweather.ui.theme.OpenMeteoWeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel by viewModels<WeatherViewModel>()

            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            val lifecycleOwner = LocalLifecycleOwner.current


            val snackBarHostState = remember { SnackbarHostState() }

            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    Log.d("TAG", "Main Activity: Disposable effect triggered")
                    if (event == Lifecycle.Event.ON_START) {
                        permissionState.launchMultiplePermissionRequest()
                        if (permissionState.allPermissionsGranted) {
                            viewModel.onEvent(WeatherEvent.FetchData)
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }


            LaunchedEffect(key1 = 1) {
                viewModel.eventFlow.collectLatest {
                    snackBarHostState.showSnackbar(it.message)
                }
            }

            OpenMeteoWeatherTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Scaffold(
                        scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState),
                    ) {
                        PermissionsRequired(
                            multiplePermissionsState = permissionState,
                            permissionsNotGrantedContent = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Need permissions to show weather",
                                        fontSize = 32.sp
                                    )
                                }
                            },
                            permissionsNotAvailableContent = {}) {
                            LaunchedEffect(key1 = true){
                                Log.d("TAG", "Main Activity: fetch weather Launched effect")
                                viewModel.onEvent(WeatherEvent.FetchData)
                            }
                            if (viewModel.weatherState.isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                WeatherCard(weatherData = viewModel.weatherState.currentData)
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    content = {
                                        items(viewModel.weatherState.hourlyForecastData) { hourlyWeather ->
                                            HourlyForecast(weatherData = hourlyWeather)
                                        }
                                    })
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    content = {
                                        items(viewModel.weatherState.dailyForecastData) { dailyWeather ->
                                            DailyForecast(weatherData = dailyWeather)
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}


