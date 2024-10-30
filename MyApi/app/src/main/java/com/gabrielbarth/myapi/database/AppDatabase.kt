package com.gabrielbarth.myapi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gabrielbarth.myapi.database.converters.DateConverters
import com.gabrielbarth.myapi.database.dao.UserLocationDao
import com.gabrielbarth.myapi.database.model.UserLocation

@Database(entities = [UserLocation::class], version = 2, exportSchema = true)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLocationDao(): UserLocationDao
}