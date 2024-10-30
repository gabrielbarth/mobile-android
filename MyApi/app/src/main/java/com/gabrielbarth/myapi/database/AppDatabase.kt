package com.gabrielbarth.myapi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gabrielbarth.myapi.database.dao.UserLocationDao
import com.gabrielbarth.myapi.database.model.UserLocation

@Database(entities = [UserLocation::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLocationDao(): UserLocationDao
}