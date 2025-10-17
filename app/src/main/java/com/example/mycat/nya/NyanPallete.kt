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
    
    @ColorInt
    const val ROBOT_HARE_BODY = 0xFFB0B0B0.toInt()
    @ColorInt
    const val ROBOT_HARE_PANEL = 0xFF606060.toInt()
    @ColorInt
    const val ROBOT_HARE_BOLT = 0xFF404040.toInt()
    @ColorInt
    const val ROBOT_HARE_EYE = 0xFF00FF00.toInt()
    @ColorInt
    const val ROBOT_HARE_EAR_INNER = 0xFFFFAAAA.toInt()
    @ColorInt
    const val ROBOT_HARE_NOSE = 0xFFFF8080.toInt()
}
