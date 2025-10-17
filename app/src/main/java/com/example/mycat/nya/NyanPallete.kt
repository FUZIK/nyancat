package com.example.mycat.nya

import android.graphics.Color
import androidx.annotation.ColorInt

object NyanPallete {
    @ColorInt
    const val SPACE = 0xFF003366.toInt()
    val LGBT_COLORS = intArrayOf(
        0xFFFF0000.toInt(),
        0xFFFF9900.toInt(),
        0xFFFFFF00.toInt(),
        0xFF33FF00.toInt(),
        0xFF0099FF.toInt(),
        0xFF6633FF.toInt()
    )
    val RAIBOW_COLORS = intArrayOf(
        0xFFFF0000.toInt(),
        0xFFFF9900.toInt(),
        0xFFFFFF00.toInt(),
        0xFF33FF00.toInt(),
        0xFF0099FF.toInt(),
        Color.BLUE,
        0xFF6633FF.toInt()
    )
    
    val RAINBOW_MODE_7_COLORS = intArrayOf(
        Color.RED,
        Color.parseColor("#FF7F00"),
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.parseColor("#8B00FF")
    )
    
    val RAINBOW_MODE_6_COLORS = intArrayOf(
        Color.RED,
        Color.parseColor("#FF7F00"),
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color.parseColor("#8B00FF")
    )
    
    val RAINBOW_MODE_5_COLORS = intArrayOf(
        Color.RED,
        Color.YELLOW,
        Color.GREEN,
        Color.parseColor("#87CEEB"),
        Color.parseColor("#8B00FF")
    )
    
    @ColorInt
    const val GRAY = 0xFF999999.toInt()
    @ColorInt
    const val BODY = 0xFFFFCC99.toInt()
    @ColorInt
    const val BODY_FILL = 0xFFFF99FF.toInt()
    @ColorInt
    const val BODY_POWDER = 0xFFFF3399.toInt()
    @ColorInt
    const val HEAD_CHEEK = 0xFFFF9999.toInt()
}
