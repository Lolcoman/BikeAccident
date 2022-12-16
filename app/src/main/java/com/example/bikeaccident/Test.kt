package com.example.bikeaccident

import android.app.Application

class Test : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    companion object {
        lateinit var instance: Test
    }
}