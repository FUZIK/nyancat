package com.example.mycat

import android.content.Context

object DI {
    lateinit var appContext: Context
        private set

    fun provide(app: App) {
        appContext = app.applicationContext
    }
}