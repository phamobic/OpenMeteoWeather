package com.hynekbraun.openmeteoweather.di

import com.hynekbraun.openmeteoweather.data.repository.WeatherRepositoryImp
import com.hynekbraun.openmeteoweather.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImp: WeatherRepositoryImp
    ): WeatherRepository
}