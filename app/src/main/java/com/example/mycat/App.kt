package com.example.mycat

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.provide(this)
    }
}