package com.example.mycat

import android.media.MediaPlayer
import java.lang.ref.WeakReference

object NyanBassProvider {
    private var bass: WeakReference<MediaPlayer>? = null
    fun bass() = bass?.get() ?: MediaPlayer.create(DI.appContext, R.raw.bee_nyancat).apply {
        isLooping = true
    }.also { bass = WeakReference(it) }
}