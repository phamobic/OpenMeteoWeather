package com.hynekbraun.openmeteoweather

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hynekbraun.openmeteoweather.presentation.mainscreen.*
import com.hynekbraun.openmeteoweather.presentation.mainscreen.util.WeatherEvent
import com.hynekbraun.openmeteoweather.ui.theme.OpenMeteoWeatherTheme
import com.hynekbraun.openmeteoweather.ui.theme.lightBackGround
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
            {
                viewModel.onEvent(WeatherEvent.FetchData)
            }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        setContent {

            val snackBarHostState = remember { SnackbarHostState() }

            OpenMeteoWeatherTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Scaffold(
                        scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState),
                    ) {
                        if (viewModel.weatherState.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier)
                            }
                        }
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            WeatherCard(weatherData = viewModel.weatherState.currentData)
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), content = {
                                items(viewModel.weatherState.hourlyForecastData) { hourlyWeather ->
                                    HourlyForecast(weatherData = hourlyWeather)
                                }
                            })
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), content = {
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
