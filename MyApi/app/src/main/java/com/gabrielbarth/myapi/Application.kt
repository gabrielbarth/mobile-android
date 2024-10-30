package com.gabrielbarth.myapi

import android.app.Application
import com.gabrielbarth.myapi.database.DatabaseBuilder

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
    }

    // fun to initiate dependencies through the context
    private fun init() {
        DatabaseBuilder.getInstance(this)
    }
}