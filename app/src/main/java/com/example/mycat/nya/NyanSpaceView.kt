package com.example.mycat.nya

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IntRange
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.plus
import androidx.core.graphics.withMatrix
import androidx.core.graphics.withSkew
import kotlin.random.Random


// https://web.archive.org/web/20120906230851/http://www.prguitarman.com/index.php?id=348
// https://nyancat.fandom.com/wiki/Home
// https://web.archive.org/web/20120903174501im_/http://www.prguitarman.com/comics/poptart1red1.gif
class NyanSpaceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
        private var PIXEL_SIZE = 30
        private var SMALL_PIXEL_DEN = PIXEL_SIZE * 0.2f
        private var MAX_WEIGHT = 0
        private var MAX_HEIGHT = 0
        private var STAR_STEP = PIXEL_SIZE * 8
        private val STAR_ANIM_INTERVAL = 70L
        private val STAR_PAINT = Paint().apply { color = Color.WHITE }
        private var isAddNewStarAllowed = true
        private lateinit var stars: ArrayList<NyanStar>
        private fun starTicker() {
                if (stars.isNotEmpty()) {
                        for (starToDraw in stars) {
                                starToDraw.frame = if (starToDraw.demon) (starToDraw.frame + 1) % 6 else starToDraw.frame + 1
                                starToDraw.offsetX  = (starToDraw.offsetX - STAR_STEP).run { if (this > 0) this else this + MAX_WEIGHT }
                        }
                        stars.removeIf { !it.demon && it.frame > 5 }
                        isAddNewStarAllowed = true
                        invalidate()
                }
                postDelayed(::starTicker, STAR_ANIM_INTERVAL)
        }
        private val RAINBOW_ANIM_INTERVAL = 70L * 2
        private lateinit var RAINBOW_PAINT: Paint
        private val RAINBOW_PIXEL_WEIGHT = 7
        private lateinit var RAINBOW_PATHS_ORIGA: List<Path>
        private lateinit var RAINBOW_PATHS_TOGGLE: List<Path>
        private var isRainbowToggled = false
        private fun rainbowTicker() {
                isRainbowToggled = !isRainbowToggled
                postDelayed(::rainbowTicker, RAINBOW_ANIM_INTERVAL)
        }

        private fun Canvas.drawStar(bX: Float, bY: Float, @IntRange(0,5) frame: Int) {
                when(frame) {
                        0 -> {
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 2f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 3f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 2f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 5f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 4f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 5f, STAR_PAINT)
                        }
                        1 -> {
                                drawStar(bX, bY, 0)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 1f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 2f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 1f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 2f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 5f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 6f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 5f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 6f, STAR_PAINT)
                        }
                        2 -> {
                                drawStar(bX, bY, 5)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 1f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 2f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 1f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 2f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 5f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 6f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 5f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 6f, STAR_PAINT)

                                drawRect(bX + PIXEL_SIZE * 3f,bY + 0f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 1f, STAR_PAINT)
                                drawRect( bX + 0f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 1f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 6f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 7f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 6f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 7f, STAR_PAINT)
                        }
                        3 -> {
                                drawRect(bX + PIXEL_SIZE * 3f,bY - 0f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 1f, STAR_PAINT)
                                drawRect( bX + 0f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 1f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 6f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 7f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 6f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 7f, STAR_PAINT)

                                drawRect( (bX + PIXEL_SIZE * 1f).plus(SMALL_PIXEL_DEN),
                                        (bY + PIXEL_SIZE * 1f).plus(SMALL_PIXEL_DEN),
                                        bX + PIXEL_SIZE * 2f,
                                        bY + PIXEL_SIZE * 2f, STAR_PAINT)
                                drawRect( bX + PIXEL_SIZE * 5f,
                                        (bY + PIXEL_SIZE * 1f).plus(SMALL_PIXEL_DEN),
                                        (bX + PIXEL_SIZE * 6f).minus(SMALL_PIXEL_DEN),
                                        bY + PIXEL_SIZE * 2f, STAR_PAINT)
                                drawRect((bX + PIXEL_SIZE * 1f).plus(SMALL_PIXEL_DEN),
                                        bY + PIXEL_SIZE * 5f,
                                        bX + PIXEL_SIZE * 2f,
                                        (bY + PIXEL_SIZE * 6f).minus(SMALL_PIXEL_DEN), STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 5f,
                                        bY + PIXEL_SIZE * 5f,
                                        (bX + PIXEL_SIZE * 6f).minus(SMALL_PIXEL_DEN),
                                        (bY + PIXEL_SIZE * 6f).minus(SMALL_PIXEL_DEN), STAR_PAINT)

                        }
                        4 -> {
                                drawRect(bX + PIXEL_SIZE * 3f,bY + 0f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 1f, STAR_PAINT)
                                drawRect( bX + 0f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 1f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 6f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 7f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 6f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 7f, STAR_PAINT)
                        }
                        5 -> {
                                drawRect(bX + PIXEL_SIZE * 3f,bY + PIXEL_SIZE * 3f,bX + PIXEL_SIZE * 4f,bY + PIXEL_SIZE * 4f, STAR_PAINT)
                        }
                        else -> throw RuntimeException("Invalid frame param")
                }
        }

        init {
                postDelayed(::starTicker, STAR_ANIM_INTERVAL)
                postDelayed(::rainbowTicker, RAINBOW_ANIM_INTERVAL)
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                super.onSizeChanged(w, h, oldw, oldh)
                MAX_WEIGHT = w
                MAX_HEIGHT = h

                stars = ArrayList<NyanStar>().run {
                        var lastY = 0f
                        repeat((MAX_HEIGHT / (PIXEL_SIZE * 7))) { starI ->
                                NyanStar(
                                        Random.nextInt(0, MAX_WEIGHT).toFloat(),
                                        Random.nextInt( lastY.toInt(), (starI + 1) * (PIXEL_SIZE * 10)).toFloat(),
                                        true).also {
                                                lastY = it.offsetY + (PIXEL_SIZE * 7)
                                        }.also(::add)
                        }
                        return@run this
                }

                val rainbowYOffset = MAX_HEIGHT / 2f - (PIXEL_SIZE * 3f * NyanPallete.LGBT_COLORS.size / 3)
                RAINBOW_PATHS_ORIGA = List(NyanPallete.LGBT_COLORS.size) {
                        Path().apply {
                                addRect(0f, 0f, PIXEL_SIZE * RAINBOW_PIXEL_WEIGHT.toFloat(), PIXEL_SIZE * 3f, Path.Direction.CW)
                                offset(PIXEL_SIZE * RAINBOW_PIXEL_WEIGHT.toFloat(), 0f)
                                addRect(0f, PIXEL_SIZE * 1f, PIXEL_SIZE * RAINBOW_PIXEL_WEIGHT.toFloat(), PIXEL_SIZE * (3f + 1f), Path.Direction.CW)
                                offset(0f, PIXEL_SIZE * 3f * it)
                                offset(0f, rainbowYOffset)
                        }
                }

                RAINBOW_PATHS_TOGGLE = List(NyanPallete.LGBT_COLORS.size) {
                        Path().apply {
                                addRect(0f, PIXEL_SIZE * 1f, PIXEL_SIZE * RAINBOW_PIXEL_WEIGHT.toFloat(), PIXEL_SIZE * (3f + 1f), Path.Direction.CW)
                                offset(PIXEL_SIZE * RAINBOW_PIXEL_WEIGHT.toFloat(), 0f)
                                addRect(0f, 0f, PIXEL_SIZE * RAINBOW_PIXEL_WEIGHT.toFloat(), PIXEL_SIZE * 3f, Path.Direction.CW)
                                offset(0f, PIXEL_SIZE * 3f * it)
                                offset(0f, rainbowYOffset)
                        }
                }

                RAINBOW_PAINT = Paint()
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
                if (isAddNewStarAllowed && event.action == MotionEvent.ACTION_DOWN) {
                        stars.add(NyanStar(event.x, event.y))
                        isAddNewStarAllowed = false
                        invalidate()
                        return true
                }
                return super.onTouchEvent(event)
        }

        override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                (if (isRainbowToggled) RAINBOW_PATHS_TOGGLE else RAINBOW_PATHS_ORIGA).forEachIndexed { index, path ->
                        canvas.drawPath(path, RAINBOW_PAINT.apply {
                                color = NyanPallete.LGBT_COLORS[index] })
                }

                for (starToDraw in stars) {
                        canvas.drawStar(starToDraw.offsetX, starToDraw.offsetY, starToDraw.frame)
                }
       }

}