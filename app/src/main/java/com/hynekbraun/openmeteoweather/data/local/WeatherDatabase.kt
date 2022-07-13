package com.hynekbraun.openmeteoweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hynekbraun.openmeteoweather.data.local.WeatherDatabase.Companion.DATABASE_VERSION

@Database(
    entities = [WeatherDataEntity::class],
    version = DATABASE_VERSION
)
abstract class WeatherDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "movies_database"
        const val DATABASE_VERSION = 1
    }

    abstract fun weatherDao(): WeatherDao
}
