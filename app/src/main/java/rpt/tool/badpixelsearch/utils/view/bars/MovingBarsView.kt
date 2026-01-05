package rpt.tool.badpixelsearch.utils.view.bars

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MovingBarsView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val density = resources.displayMetrics.density
    private var topMargin = 0f

    private val barSize = 15f * density
    private val gap = barSize
    private val speed = 1.2f * density

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = false
    }

    private var offset = 0f
    private var verticalBars = false

    private val colors = listOf(
        Color.WHITE, Color.RED, Color.GREEN, Color.BLUE,
        Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.GRAY
    )
    private var currentColor = 0

    var fpsCallback: ((Int) -> Unit)? = null
    private var fpsTimer = 0L
    private var frameCounter = 0



    private fun getTopOnScreen(): Float {
        val loc = IntArray(2)
        getLocationInWindow(loc)
        return loc[1].toFloat()
    }

    init {
        setOnClickListener {
            currentColor++
            if (currentColor >= colors.size) {
                currentColor = 0
                verticalBars = !verticalBars
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = colors[currentColor]

        if (!verticalBars) drawHorizontal(canvas)
        else drawVertical(canvas)

        animateBars()
        calcFPS()
        invalidate()
    }

    fun setTopMargin(margin: Float) {
        topMargin = margin
        invalidate()
    }

    private fun drawHorizontal(canvas: Canvas) {
        var y = height - offset

        while (y > -barSize) {

            if (y < topMargin) {
                y -= (barSize + gap)
                continue
            }

            canvas.drawRect(
                0f,
                y,
                width.toFloat(),
                y + barSize,
                paint
            )

            y -= (barSize + gap)
        }
    }


    private fun drawVertical(canvas: Canvas) {
        var x = width - offset

        while (x > -barSize) {

            canvas.drawRect(
                x,
                topMargin,
                x + barSize,
                height.toFloat(),
                paint
            )

            x -= (barSize + gap)
        }
    }

    private fun animateBars() {
        offset += speed
        if (offset > barSize + gap) offset = 0f
    }

    private fun calcFPS() {
        frameCounter++
        val now = System.nanoTime()
        if (fpsTimer == 0L) fpsTimer = now
        if (now - fpsTimer >= 1_000_000_000L) {
            fpsCallback?.invoke(frameCounter)
            frameCounter = 0
            fpsTimer = now
        }
    }
}
