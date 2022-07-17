package com.hynekbraun.openmeteoweather.di

import com.hynekbraun.openmeteoweather.data.location.CurrentLocationManagerImp
import com.hynekbraun.openmeteoweather.domain.CurrentLocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Provides
    @Singleton
    abstract fun provideCurrentLocationManager(
        currentLocationManagerImp: CurrentLocationManagerImp
    ): CurrentLocationManager


}