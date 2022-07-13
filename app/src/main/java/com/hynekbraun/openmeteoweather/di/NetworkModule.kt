package com.hynekbraun.openmeteoweather.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val REQUEST_TIMEOUT_SEC = 30L
    private const val MAX_PARALLEL_REQUESTS = 5
    const val BASE_URL: String = "https://api.open-meteo.com/"

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            // Setup Timeouts
            connectTimeout(REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)
            readTimeout(REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)
            writeTimeout(REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)

            // Setup Max Requests
            dispatcher(Dispatcher().apply { maxRequests = MAX_PARALLEL_REQUESTS })
            retryOnConnectionFailure(false)
        }.build()
    }


}