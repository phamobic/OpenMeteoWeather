package com.hynekbraun.openmeteoweather.di

import com.hynekbraun.openmeteoweather.data.network.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi{
        return retrofit.create()
    }
}