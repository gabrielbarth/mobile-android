package com.gabrielbarth.myapi.database

import android.content.Context
import androidx.room.Room
import java.lang.Exception

object DatabaseBuilder {
    private var instance: AppDatabase? = null
    fun getInstance(context: Context? = null): AppDatabase {
        return instance ?: synchronized(this) {
            if (context == null) {
                throw Exception("DatabaseBuilder.getInstance(context) should be initialized before use it")
            }
            val newInstance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
                .build()
            instance = newInstance
            newInstance
        }
    }
}