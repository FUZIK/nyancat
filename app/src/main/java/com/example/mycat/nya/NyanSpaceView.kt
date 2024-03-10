package com.example.mycat.nya

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.*
import com.example.mycat.QueueLinearFloodFiller
import com.example.mycat.R
import splitties.experimental.InternalSplittiesApi
import splitties.views.backgroundColor
import splitties.views.dsl.core.*
import splitties.views.dsl.idepreview.UiPreView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

typealias RainbowLine = Pair<Path, Int>

private class NyanSpaceViewPreview(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : UiPreView(
        context = context.withTheme(R.style.Theme_MyCat),
        attrs = attrs,
        defStyleAttr = defStyleAttr,
        createUi = {
                object : Ui {
                        override val ctx: Context = it
                        override val root = frameLayout {
                                backgroundColor = NyanPallete.SPACE
                                add(
                                        NyanSpaceView(it),
                                        lParams(matchParent, matchParent)
                                )
                        }
                }
        }
)

private class NyanStar(
        var offsetX: Float,
        var offsetY: Float,
        var frame: Int = MIN_FRAME
) {
        companion object {
                const val MAX_FRAME = 5
                const val MIN_FRAME = 0
        }
}
// https://web.archive.org/web/20120906230851/http://www.prguitarman.com/index.php?id=348
// https://nyancat.fandom.com/wiki/Home
// https://web.archive.org/web/20120903174501im_/http://www.prguitarman.com/comics/poptart1red1.gif
class NyanSpaceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
        companion object {
                private const val STAR_ANIM_INTERVAL = 100L
                private const val RAINBOW_ANIM_INTERVAL = STAR_ANIM_INTERVAL * 2

                private const val NYAN_SPRITES = 5

                private const val PIXEL_DP = 10F

                private const val STAR_STEP_PIXELS = 7

                private const val RAINBOW_PIXEL_WEIGHT = 7
                private const val RAINBOW_PIXEL_HEIGHT =
                        3.333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333f

                private val PAW_0_COLORED = intArrayOf(
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK, 0,
                        0, Color.BLACK, Color.BLACK, 0, 0
                )

                private val PAW_1_COLORED = intArrayOf(
                        0, 0, Color.BLACK, 0, 0,
                        0, Color.BLACK, NyanPallete.GRAY, Color.BLACK, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK, 0,
                        Color.BLACK, Color.BLACK, Color.BLACK, 0, 0
                )

                private val PAW_2_COLORED = intArrayOf(
                        0, 0, Color.BLACK, 0, 0,
                        0, Color.BLACK, Color.BLACK, Color.BLACK, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK, 0,
                        Color.BLACK, Color.BLACK, Color.BLACK, 0, 0
                )

                private val PAW_3_COLORED = intArrayOf(
                        0, 0, 0, 0, 0,
                        0, Color.BLACK, 0, 0, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, 0, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK, 0,
                        Color.BLACK, Color.BLACK, Color.BLACK, 0, 0
                )

                private val PAW_4_COLORED = intArrayOf(
                        0, 0, 0, 0, 0,
                        Color.BLACK, 0, 0, 0, 0,
                        Color.BLACK, NyanPallete.GRAY, 0, 0, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK, 0,
                        Color.BLACK, Color.BLACK, Color.BLACK, 0, 0
                )

                private val PAW_5_COLORED = intArrayOf(
                        0, 0, 0, 0, 0,
                        0, Color.BLACK, Color.BLACK, 0, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, NyanPallete.GRAY, 0,
                        Color.BLACK, NyanPallete.GRAY, NyanPallete.GRAY, Color.BLACK, Color.BLACK,
                        Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, 0
                )
        }

        private val aPaint = Paint()
        private var pixelSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                PIXEL_DP, resources.displayMetrics
        ).toInt()
        private var smallPixelSize = pixelSize * 0.2f
        private inline val Float.pxa: Float get() = this * pixelSize
        private inline val Int.pxa: Int get() = this * pixelSize
        private var weight = 0
        private var height = 0
        private var starSpriteHeight = pixelSize * 7
        private var starStepOffset = pixelSize * STAR_STEP_PIXELS
        private var daemonStars: List<NyanStar> = emptyList()
        private var isUserStarsEnabled = true
        private val linkedUserStars = LinkedList<NyanStar>()
        private var rainbowPaths: List<RainbowLine> = emptyList()
        private var isRainbowToggledFrame = false
        private val hvostYOffset = 9f.pxa
        private val hvostXOffset = 6f.pxa
        private var rainbowSegmentWeight: Float = 0f
        private var rainbowDoubleSegmentHeight: Float = 0f
        private var rainbowDoubleSegmentWeight: Float = 0f
        private var rainbowDoubleSegments: Int = 0
        private var rainbowWeight: Float = 0f
        private var rainbowHeight: Float = 0f
        private var rainbowToggledFramePrivotX: Float = 0f
        private var rainbowYOffset: Float = 0f
        private var rainbowXOffset: Float = 0f
        private var nyanWeight: Float = 0f
        private var nyanXOffset: Float = 0f
        private var nyanYOffset: Float = 0f
        private var curNyanSprite = 0
        private val headHeight = 13f.pxa
        private val headWeight = 16f.pxa
        private val bodyheight = 18f.pxa
        private val bodyweight = 21f.pxa
        private val innerbodyweight = bodyweight - 2f.pxa
        private val innerbodyheight = bodyheight - 2f.pxa
        private val hvostHeight = 6f.pxa
        private var cubeForHvostSprite1 = Rect()
        private val nyanCatPowderDefaults = arrayOf(
                Rect(0, 0, 1.2f.pxa.toInt(), 1.2f.pxa.toInt()),
                Rect(0, 0, 1.2f.pxa.toInt(), 1f.pxa.toInt()),
                Rect(0, 0, 1f.pxa.toInt(), 1.2f.pxa.toInt())
        )
        private val nyanCatPowderSprites = ArrayList<Rect>(16).also { b ->
                fun distance(a: Rect, b: Rect): Double {
                        val y1 = a.top + ((a.bottom - a.top) / 2)
                        val y2 = b.top + ((b.bottom - b.top) / 2)
                        val x1 = a.left + ((a.right - a.left) / 2)
                        val x2 = b.left + ((b.right - b.left) / 2)
                        return sqrt(
                                (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1) * 1.0
                        )
                }
                repeat(16) {
                        val s = nyanCatPowderDefaults.random()
                        var r: Rect
                        do {
                                r = Rect(s)
                                r.offset(
                                        Random.nextInt(1.pxa, (innerbodyweight - 1.pxa).toInt()),
                                        Random.nextInt(1.pxa, (innerbodyheight - 1.pxa).toInt())
                                )
                                if (b.isEmpty()) {
                                        b.add(r)
                                        break
                                }
                        } while (b.any { Rect.intersects(it, r) || distance(it, r) <= 2.4f.pxa })
                        b.add(r)
                }
        }

        private fun Canvas.drawStar(bX: Float, bY: Float, frame: Int) {
                aPaint.apply { color = Color.WHITE }
                when(frame) {
                        0 -> drawRect(
                                bX + pixelSize * 3f,
                                bY + pixelSize * 3f,
                                bX + pixelSize * 4f,
                                bY + pixelSize * 4f,
                                aPaint
                        )
                        1 -> {
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 2f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 3f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 2f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 5f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 4f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 5f,
                                        aPaint
                                )
                        }

                        2 -> {
                                drawStar(bX, bY, 0)
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 1f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 2f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 1f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 2f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 5f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 6f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 5f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 6f,
                                        aPaint
                                )
                        }
                        3 -> {
                                drawStar(bX, bY, 5)
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 1f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 2f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 1f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 2f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 5f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 6f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 5f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 6f,
                                        aPaint
                                )

                                drawRect(bX + pixelSize * 3f, bY + 0f, bX + pixelSize * 4f, bY + pixelSize * 1f, aPaint)
                                drawRect(bX + 0f, bY + pixelSize * 3f, bX + pixelSize * 1f, bY + pixelSize * 4f, aPaint)
                                drawRect(
                                        bX + pixelSize * 6f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 7f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 6f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 7f,
                                        aPaint
                                )
                        }
                        4 -> {
                                drawRect(bX + pixelSize * 3f, bY - 0f, bX + pixelSize * 4f, bY + pixelSize * 1f, aPaint)
                                drawRect(bX + 0f, bY + pixelSize * 3f, bX + pixelSize * 1f, bY + pixelSize * 4f, aPaint)
                                drawRect(
                                        bX + pixelSize * 6f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 7f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 6f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 7f,
                                        aPaint
                                )

                                drawRect(
                                        (bX + pixelSize * 1f).plus(smallPixelSize),
                                        (bY + pixelSize * 1f).plus(smallPixelSize),
                                        bX + pixelSize * 2f,
                                        bY + pixelSize * 2f, aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 5f,
                                        (bY + pixelSize * 1f).plus(smallPixelSize),
                                        (bX + pixelSize * 6f).minus(smallPixelSize),
                                        bY + pixelSize * 2f, aPaint
                                )
                                drawRect(
                                        (bX + pixelSize * 1f).plus(smallPixelSize),
                                        bY + pixelSize * 5f,
                                        bX + pixelSize * 2f,
                                        (bY + pixelSize * 6f).minus(smallPixelSize), aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 5f,
                                        bY + pixelSize * 5f,
                                        (bX + pixelSize * 6f).minus(smallPixelSize),
                                        (bY + pixelSize * 6f).minus(smallPixelSize), aPaint
                                )

                        }
                        5 -> {
                                drawRect(bX + pixelSize * 3f, bY + 0f, bX + pixelSize * 4f, bY + pixelSize * 1f, aPaint)
                                drawRect(bX + 0f, bY + pixelSize * 3f, bX + pixelSize * 1f, bY + pixelSize * 4f, aPaint)
                                drawRect(
                                        bX + pixelSize * 6f,
                                        bY + pixelSize * 3f,
                                        bX + pixelSize * 7f,
                                        bY + pixelSize * 4f,
                                        aPaint
                                )
                                drawRect(
                                        bX + pixelSize * 3f,
                                        bY + pixelSize * 6f,
                                        bX + pixelSize * 4f,
                                        bY + pixelSize * 7f,
                                        aPaint
                                )
                        }
                        else -> throw RuntimeException("Invalid frame param")
                }
        }

        private fun Canvas.drawRainbow() {
                rainbowPaths.forEach { (path, color) ->
                        aPaint.color = color
                        drawPath(path, aPaint)
                }
        }

        private fun Canvas.drawPawSprite(frame: Int) {
                fun getScaledBitmap(coloredMatrix: IntArray, width: Int, height: Int) =
                        Bitmap.createBitmap(coloredMatrix, width, height, Bitmap.Config.ARGB_8888).run {
                                scale(width.pxa, height.pxa, false)
                        }
                when (frame) {
                        0 -> drawBitmap(getScaledBitmap(PAW_0_COLORED, 5, 5), 0f, 0f, aPaint)
                        1 -> drawBitmap(getScaledBitmap(PAW_1_COLORED, 5, 5), 0f, 0f, aPaint)
                        2 -> drawBitmap(getScaledBitmap(PAW_2_COLORED, 5, 5), 0f, 0f, aPaint)
                        3 -> drawBitmap(getScaledBitmap(PAW_3_COLORED, 5, 5), 0f, 0f, aPaint)
                        4 -> drawBitmap(getScaledBitmap(PAW_4_COLORED, 5, 5), 0f, 0f, aPaint)
                        5 -> drawBitmap(getScaledBitmap(PAW_5_COLORED, 5, 5), 0f, 0f, aPaint)
                        6 -> withScale(-1f, 1f, 2f.pxa, 0f) { drawPawSprite(4) }
                }
        }

        private fun Canvas.drawNyanHead() {
                aPaint.apply { color = Color.BLACK }

                fun Canvas.drawFiftyHead() {
                        withSave {
                                translate(2f.pxa, 0f.pxa)
                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                translate(2f.pxa, 1f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(1f.pxa, 1f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(1f.pxa, 1f.pxa)
                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                        }
                        withSave {
                                translate(1f.pxa, 1f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 4f.pxa, aPaint)
                                translate(-1f.pxa, 4f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 5f.pxa, aPaint)
                                translate(1f.pxa, 5f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(1f.pxa, 1f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(1f.pxa, 1f.pxa)
                                drawRect(0f, 0f, 6f.pxa, 1f.pxa, aPaint)
                        }
                }

                fun Canvas.drawEye() {
                        aPaint.color = Color.WHITE
                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                        aPaint.color = Color.BLACK
                        translate(0f, 1f.pxa)
                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                        translate(1f.pxa, 0f)
                        drawRect(0f, -1f.pxa, 1f.pxa, 1f.pxa, aPaint)
                }

                Bitmap.createBitmap(headWeight.toInt(), headHeight.toInt(), Bitmap.Config.ARGB_8888).applyCanvas {
                        drawFiftyHead()
                        withScale(-1f, 1f, headWeight / 2, 0f) {
                                drawFiftyHead()
                        }

                        withSave {
                                translate(4f.pxa, 6f.pxa)
                                drawEye()
                                translate(4f.pxa, 0f)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(2f.pxa, -1f.pxa)
                                drawEye()
                        }

                        withSave {
                                aPaint.color = NyanPallete.HEAD_CHEEK
                                translate(2f.pxa, 8f.pxa)
                                drawRect(0f, 0f, 2f.pxa, 2f.pxa, aPaint)
                                translate(2f.pxa, 0f)
                                translate(9f.pxa, 0f)
                                drawRect(0f, 0f, 2f.pxa, 2f.pxa, aPaint)
                        }

                        withSave {
                                aPaint.color = Color.BLACK
                                translate(5f.pxa, 9f.pxa)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(3f.pxa, 0f)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(3f.pxa, 0f)
                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                translate(1f.pxa, 1f.pxa)
                                drawRect(-7f.pxa, 0f, 0f, 1f.pxa, aPaint)
                        }
                }.also { bitmap ->
                        QueueLinearFloodFiller(bitmap, Color.TRANSPARENT, NyanPallete.GRAY)
                                .floodFill(headWeight.toInt() / 2, headHeight.toInt() / 2)
                        drawBitmap(bitmap, 0f, 0f, aPaint)
                }
        }

        private fun Canvas.drawNyanBody() {
                aPaint.color = NyanPallete.BODY
                withSave {
                        translate(1f.pxa, 1f.pxa)
                        drawRect(0f, 0f, bodyweight, bodyheight, aPaint)
                }

                aPaint.color = Color.BLACK
                withSave {
                        translate(0f, 2f.pxa)
                        drawRect(0f, 0f, 1f.pxa, bodyheight - 2f.pxa, aPaint)
                        translate(1f.pxa, 0f)
                        translate(bodyweight, 0f)
                        drawRect(0f, 0f, 1f.pxa, bodyheight - 2f.pxa, aPaint)
                }
                withSave {
                        translate(2f.pxa, 0f)
                        drawRect(0f, 1f.pxa, bodyweight - 2f.pxa, 0f, aPaint)
                        translate(0f, 1f.pxa)
                        translate(0f, bodyheight)
                        drawRect(0f, 1f.pxa, bodyweight - 2f.pxa, 0f, aPaint)
                }

                withSave {
                        translate(1f.pxa, 1f.pxa)
                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                        translate(0f, -1f.pxa)
                        translate(0f, bodyheight)
                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                }
                withSave {
                        translate(bodyweight, 1f.pxa)
                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                        translate(0f, -1f.pxa)
                        translate(0f, bodyheight)
                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                }
                aPaint.color = NyanPallete.BODY_FILL
                withSave {
                        translate(2f.pxa, 2f.pxa)
                        drawRect(2f.pxa, 0f, innerbodyweight - 2f.pxa, innerbodyheight, aPaint)
                        drawRect(0f, 2f.pxa, innerbodyweight, innerbodyheight - 2f.pxa, aPaint)
                        drawRect(1f.pxa, 1f.pxa, innerbodyweight - 1f.pxa, innerbodyheight - 1f.pxa, aPaint)
                        aPaint.color = NyanPallete.BODY_POWDER
                        nyanCatPowderSprites.forEach { drawRect(it, aPaint) }
                }
        }

        private fun Canvas.drawHvostSprite(frame: Int) {
                aPaint.color = Color.BLACK
                when (frame) {
                        0 -> {
                                withSave {
                                        aPaint.color = NyanPallete.GRAY
                                        withSave {
                                                repeat(4) {
                                                        translate(1f.pxa, 1f.pxa)
                                                        drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                                }
                                        }
                                        aPaint.color = Color.BLACK
                                        drawRect(0f, 0f, 4f.pxa, 1f.pxa, aPaint)
                                        withSave {
                                                translate(0f, 1f.pxa)
                                                repeat(4) {
                                                        drawRect(0f, 0f, 1f.pxa, 2f.pxa, aPaint)
                                                        translate(1f.pxa, 1f.pxa)
                                                }
                                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                                translate(1f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                        }
                                        translate(3f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                        translate(1f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                        translate(1f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                }
                        }

                        1 -> {
                                withSave {
                                        aPaint.color = NyanPallete.GRAY
                                        withSave {
                                                translate(1f.pxa, 2f.pxa)
                                                drawRect(cubeForHvostSprite1, aPaint)
                                                translate(1f.pxa, 2f.pxa)
                                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                                translate(2f.pxa, 0f)
                                                drawRect(cubeForHvostSprite1, aPaint)
                                        }
                                        aPaint.color = Color.BLACK
                                        translate(0f, 1f.pxa)
                                        withSave {
                                                translate(0f, 1f.pxa)
                                                drawRect(0f, 0f, 1f.pxa, 2f.pxa, aPaint)
                                                translate(1f.pxa, 2f.pxa)
                                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                                translate(1f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                                translate(2f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                        }
                                        translate(1f.pxa, 0f)
                                        drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                        translate(2f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 1f.pxa, 2f.pxa, aPaint)
                                        translate(1f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                }
                        }

                        2 -> {
                                withSave {
                                        aPaint.color = NyanPallete.GRAY
                                        withSave {
                                                translate(2f.pxa, 5f.pxa)
                                                drawRect(0f, 0f, 4f.pxa, 1f.pxa, aPaint)
                                                translate(-1f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 3f.pxa, 1f.pxa, aPaint)
                                        }
                                        aPaint.color = Color.BLACK
                                        translate(0f, 3f.pxa)
                                        withSave {
                                                translate(0f, 2f.pxa)
                                                drawRect(0f, 0f, 1f.pxa, 2f.pxa, aPaint)
                                                translate(1f.pxa, 2f.pxa)
                                                drawRect(0f, 0f, 4f.pxa, 1f.pxa, aPaint)
                                                translate(3f.pxa, -1f.pxa)
                                                drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                        }
                                        translate(5f.pxa, 0f)
                                        drawRect(0f, 0f, 1f.pxa, 2f.pxa, aPaint)
                                        translate(-3f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 3f.pxa, 1f.pxa, aPaint)
                                        translate(-1f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                }
                        }

                        3 -> {
                                withSave {
                                        translate(0f, 4f.pxa)
                                        withScale(1f, -1f, 0f, hvostHeight / 2) {
                                                drawHvostSprite(1)
                                        }
                                }
                        }

                        4 -> {
                                withSave {
                                        aPaint.color = NyanPallete.GRAY
                                        withSave {
                                                translate(1f.pxa, 2f.pxa)
                                                drawRect(0f, 0f, 3f.pxa, 1f.pxa, aPaint)
                                                translate(1f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 4f.pxa, 1f.pxa, aPaint)
                                                translate(4f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                        }
                                        aPaint.color = Color.BLACK
                                        translate(0f, 1f.pxa)
                                        withSave {
                                                translate(1f.pxa, 0f)
                                                drawRect(0f, 0f, 4f.pxa, 1f.pxa, aPaint)
                                                translate(3f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 3f.pxa, 1f.pxa, aPaint)
                                                translate(2f.pxa, 1f.pxa)
                                                drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                        }
                                        translate(0f, 1f.pxa)
                                        drawRect(0f, 0f, 1f.pxa, 2f.pxa, aPaint)
                                        translate(1f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 1f.pxa, 1f.pxa, aPaint)
                                        translate(1f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 4f.pxa, 1f.pxa, aPaint)
                                        translate(3f.pxa, 1f.pxa)
                                        drawRect(0f, 0f, 2f.pxa, 1f.pxa, aPaint)
                                }
                        }
                }
        }

        private fun Canvas.drawPawPairSprite(frame: Int) {
                when (frame) {
                        0 -> withSave {
                                // left 1
                                translate(4f.pxa, 17f.pxa)
                                drawPawSprite(5)
                                // left 2
                                translate(5f.pxa, 0f.pxa)
                                translate(1f.pxa, 0f.pxa)
                                drawPawSprite(4)
                                // right 1
                                translate(5f.pxa, 0f.pxa)
                                translate(6f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(0)
                        }

                        1 -> withSave {
                                // left 1
                                translate(5f.pxa, 17f.pxa)
                                drawPawSprite(3)
                                // left 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 1
                                translate(5f.pxa, 0f.pxa)
                                translate(6f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(6)
                        }

                        2 -> withSave {
                                translate(1f.pxa, 0f)
                                // left 1
                                translate(5f.pxa, 17f.pxa)
                                drawPawSprite(4)
                                // left 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 1
                                translate(5f.pxa, 0f.pxa)
                                translate(6f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(6)
                        }

                        3 -> withSave {
                                // left 1
                                translate(3f.pxa, 17f.pxa)
                                drawPawSprite(2)
                                // left 2
                                translate(5f.pxa, 0f.pxa)
//                                translate(1f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 1
                                translate(5f.pxa, 0f.pxa)
                                translate(7f.pxa, 0f.pxa)
                                drawPawSprite(6)
                                // right 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(6)
                        }

                        4 -> withSave {
                                // left 1
                                translate(3f.pxa, 17f.pxa)
                                drawPawSprite(1)
                                // left 2
                                translate(5f.pxa, 0f.pxa)
//                                translate(1f.pxa, 0f.pxa)
                                drawPawSprite(4)
                                // right 1
                                translate(5f.pxa, 0f.pxa)
                                translate(7f.pxa, 0f.pxa)
                                drawPawSprite(4)
                                // right 2
                                translate(5f.pxa, 0f.pxa)
                                drawPawSprite(6)
                        }
                }
        }

        private fun Canvas.compositeDrawNyanSprite(
                bX: Float, bY: Float,
                hX: Float, hY: Float,
                hvostFrame: Int,
                pawPair: Int = 0
        ) {
                withSave {
                        translate(0f, hvostYOffset)
                        drawHvostSprite(hvostFrame)
                }
                withSave {
                        translate(0f, bY)
                        drawPawPairSprite(pawPair)
                }
                withSave {
                        translate(hvostXOffset + bX, 0f + bY)
                        drawNyanBody()
                }
                withSave {
                        translate(hvostXOffset + bX, 0f)
                        translate(12f.pxa + hX, 7f.pxa + hY)
                        drawNyanHead()
                }
        }

        private fun Canvas.drawNyanSprite(frame: Int) {
                when (frame) {
                        0 -> compositeDrawNyanSprite(0f, 0f, 0f, 0f, 0, 0)
                        1 -> compositeDrawNyanSprite(0f, 0f, 1f.pxa, 0f, 1, 1)
                        2 -> compositeDrawNyanSprite(0f, 1f.pxa, 1f.pxa, 1f.pxa, 2, 2)
                        3 -> compositeDrawNyanSprite(0f, 1f.pxa, 1f.pxa, 1f.pxa, 3, 1)
                        4 -> compositeDrawNyanSprite(0f, 1f.pxa, 0f, 1f.pxa, 4, 3)
                        5 -> compositeDrawNyanSprite(0f, 1f.pxa, 0f, -0.2f.pxa, 1, 4)
                }
        }

        private val starTickerR = object : Runnable {
                override fun run() {
                        for (starToDraw in linkedUserStars) {
                                starToDraw.frame += 1
                                starToDraw.offsetX =
                                        (starToDraw.offsetX - starStepOffset).run { if (this > 0) this else this + weight }
                        }
                        for (starToDraw in daemonStars) {
                                starToDraw.frame = (starToDraw.frame + 1) % NyanStar.MAX_FRAME
                                starToDraw.offsetX =
                                        (starToDraw.offsetX - starStepOffset).run { if (this > 0) this else this + weight }
                        }
                        curNyanSprite = (curNyanSprite + 1) % NYAN_SPRITES
                        clearInvalidUserStar()
                        invalidate()
                        postDelayed(this, STAR_ANIM_INTERVAL)
                }
        }

        private val rainbowTickerR = object : Runnable {
                override fun run() {
                        isRainbowToggledFrame = !isRainbowToggledFrame
                        postDelayed(this, RAINBOW_ANIM_INTERVAL)
                }
        }

        private fun addUserStar(star: NyanStar) {
                linkedUserStars.addLast(star)
        }

        private fun clearInvalidUserStar() {
                while (linkedUserStars.isNotEmpty()
                        && linkedUserStars.first.frame > NyanStar.MAX_FRAME
                )
                        linkedUserStars.removeFirst()
        }

        fun play() {
                post(starTickerR)
                post(rainbowTickerR)
        }

        fun pause() {
                removeCallbacks(starTickerR)
                removeCallbacks(rainbowTickerR)
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                super.onSizeChanged(w, h, oldw, oldh)
                weight = w
                height = h

                cubeForHvostSprite1 = Rect(0, 0, 2.pxa, 2.pxa)

                val starsToDaemon = height / starSpriteHeight
                daemonStars = ArrayList<NyanStar>().run {
                        var lastY = 0f
                        repeat(starsToDaemon) { i ->
                                val until = (i + 1) * (pixelSize * 10)
                                NyanStar(
                                        (Math.random() * weight).toFloat(),
                                        (lastY + (Math.random() * until)).toFloat()
                                                .also { lastY = it + starSpriteHeight })
                                        .also(::add)
                        }
                        return@run this
                }

                rainbowSegmentWeight = pixelSize * RAINBOW_PIXEL_WEIGHT * 1f
                rainbowDoubleSegmentHeight = pixelSize * RAINBOW_PIXEL_HEIGHT * 1f
                rainbowDoubleSegmentWeight = rainbowSegmentWeight * 2f
                rainbowDoubleSegments = weight / 2 / rainbowDoubleSegmentWeight.toInt()
                rainbowWeight = rainbowDoubleSegmentWeight * rainbowDoubleSegments
                rainbowHeight = rainbowDoubleSegmentHeight * NyanPallete.LGBT_COLORS.size
                rainbowXOffset = -(rainbowSegmentWeight / 2)
                rainbowYOffset = (height / 2f) - (rainbowHeight * 0.3f)
                rainbowToggledFramePrivotX = (rainbowWeight / 2) + rainbowXOffset
                rainbowPaths = Path().apply {
                        addRect(
                                0f,
                                pixelSize.toFloat(),
                                rainbowSegmentWeight,
                                rainbowDoubleSegmentHeight + pixelSize,
                                Path.Direction.CCW
                        )
                        offset(rainbowSegmentWeight, 0f)
                        addRect(0f, 0f, rainbowSegmentWeight, rainbowDoubleSegmentHeight, Path.Direction.CCW)
                }.run {
                        Path().apply {
                                repeat(rainbowDoubleSegments) {
                                        offset(rainbowDoubleSegmentWeight, 0f)
                                        addPath(this@run)
                                }
                                offset(rainbowXOffset, rainbowYOffset)
                        }
                }.run {
                        List(NyanPallete.LGBT_COLORS.size) { i ->
                                Path(this@run).apply {
                                        offset(
                                                0f,
                                                rainbowDoubleSegmentHeight * i
                                        )
                                } to NyanPallete.LGBT_COLORS[i]
                        }
                }

                nyanWeight = 20f.pxa
                nyanXOffset = rainbowWeight - abs(rainbowXOffset) - 8f.pxa // - ~hvost
                nyanYOffset = rainbowYOffset - 1f.pxa
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
                if (isUserStarsEnabled && event.action == MotionEvent.ACTION_DOWN) {
                        addUserStar(NyanStar(event.x - starSpriteHeight, event.y - starSpriteHeight / 2))
                        invalidate()
                        isUserStarsEnabled = false
                        postDelayed({ isUserStarsEnabled = true }, STAR_ANIM_INTERVAL)
                        return true
                }
                return super.onTouchEvent(event)
        }

        override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)

//                println("NyanView onDraw")
//                canvas.withSave { // лапки
//                        repeat(7) {
//                                canvas.drawPawSprite(it)
//                                canvas.translate(0f, 9f.pxa)
//                        }
//                }
//                canvas.withSave { // хвосты
//                        repeat(5) {
//                                canvas.drawHvostSprite(it)
//                                canvas.translate(9f.pxa, 0f)
//                        }
//                }
//                removeCallbacks(starTickerR)
//                removeCallbacks(rainbowTickerR)
//                return

                if (isRainbowToggledFrame) {
                        canvas.withScale(-1f, 1f, rainbowToggledFramePrivotX, 0f) {
                                drawRainbow()
                        }
                } else {
                        canvas.drawRainbow()
                }

                for (starToDraw in daemonStars) {
                        canvas.drawStar(starToDraw.offsetX, starToDraw.offsetY, starToDraw.frame)
                }
                for (starToDraw in linkedUserStars) {
                        canvas.drawStar(starToDraw.offsetX, starToDraw.offsetY, starToDraw.frame)
                }

                canvas.withSave {
                        translate(nyanXOffset, nyanYOffset)
                        canvas.drawNyanSprite(curNyanSprite)
                }
        }
}

