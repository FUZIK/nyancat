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
        0xFFFF0000.toInt(),
        0xFFFF7F00.toInt(),
        0xFFFFFF00.toInt(),
        0xFF00FF00.toInt(),
        0xFF00FFFF.toInt(),
        0xFF0000FF.toInt(),
        0xFF8B00FF.toInt()
    )
    
    val RAINBOW_MODE_6_COLORS = intArrayOf(
        0xFFFF0000.toInt(),
        0xFFFF7F00.toInt(),
        0xFFFFFF00.toInt(),
        0xFF00FF00.toInt(),
        0xFF0000FF.toInt(),
        0xFF8B00FF.toInt()
    )
    
    val RAINBOW_MODE_5_COLORS = intArrayOf(
        0xFFFF0000.toInt(),
        0xFFFFFF00.toInt(),
        0xFF00FF00.toInt(),
        0xFF87CEEB.toInt(),
        0xFF8B00FF.toInt()
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
