package com.example.mycat

import android.app.Activity
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.mycat.nya.NyanPallete
import com.example.mycat.nya.NyanSpaceView
import splitties.dimensions.dp
import splitties.mainhandler.mainHandler
import splitties.views.backgroundColor
import splitties.views.dsl.core.*
import splitties.views.gravityTopCenter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

class SpacedNyash : Activity() {
    private lateinit var store: KittyStore
    private lateinit var timer: Timer
    private lateinit var nyanView: NyanSpaceView
    private var nyanBass: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        store = KittyStore.factory(applicationContext)
        contentView = frameLayout {
            backgroundColor = NyanPallete.SPACE
            add(
                NyanSpaceView(this@SpacedNyash).also { nyanView = it },
                lParams(matchParent, matchParent)
            )
            add(textView {
                textSize = dp(15f)
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(0x43000000)
                isClickable = true
            }.also { nyasnost ->
                ViewCompat.setOnApplyWindowInsetsListener(nyasnost) { view, windowInsets ->
                    windowInsets.getInsets(WindowInsetsCompat.Type.statusBars()).also {
                        view.updatePadding(top = it.top)
                        view.updatePadding(top = it.top)
                    }
                    WindowInsetsCompat.CONSUMED
                }
                timer = fixedRateTimer(period = TimeUnit.SECONDS.toMillis(1)) {
                    mainHandler.post {
                        nyasnost.text = getString(R.string.nyashes, store.plusNyash().toString())
                    }
                }
            }, lParams(wrapContent, wrapContent, gravityTopCenter))
        }
        nyanBass = MediaPlayer.create(applicationContext, R.raw.bee_nyancat).apply {
            isLooping = true
        }
    }

    override fun onResume() {
        nyanView.play()
        nyanBass?.run { if (!isPlaying) start() }
        super.onResume()
    }

    override fun onPause() {
        nyanView.pause()
        nyanBass?.run { if (isPlaying) pause() }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        nyanBass?.release()
        timer.cancel()
    }
}