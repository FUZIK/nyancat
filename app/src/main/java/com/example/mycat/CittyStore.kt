package com.example.mycat

import android.content.Context
import android.content.SharedPreferences

class KittyStore(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        fun factory(context: Context) = KittyStore(context.getSharedPreferences("kitty", Context.MODE_PRIVATE))
    }

    var nyashes: Long
        private set(value) = sharedPreferences.edit().putLong("nyash", value).apply()
        get() = sharedPreferences.getLong("nyash", 0L)

    fun plusNyash() = ++nyashes
}