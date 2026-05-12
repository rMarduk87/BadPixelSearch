package rpt.tool.badpixelsearch.utils.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toColorInt

class TouchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val touchPoints = mutableMapOf<Int, Pair<Float, Float>>()
    var onFingerChange: ((Int) -> Unit)? = null
    var topExclusion = 0f

    // 🔵 linea sottile stile debug
    private val linePaint = Paint().apply {
        color = "#60A5FA".toColorInt()
        strokeWidth = 1.5f
        isAntiAlias = true
    }

    // 🔵 cerchio principale
    private val fillPaint = Paint().apply {
        color = "#60649e".toColorInt()
        alpha = 220
        isAntiAlias = true
    }

    // 🔵 glow esterno
    private val glowPaint = Paint().apply {
        color = "#3B82F6".toColorInt()
        alpha = 80
        isAntiAlias = true
        maskFilter = android.graphics.BlurMaskFilter(25f, android.graphics.BlurMaskFilter.Blur.NORMAL)
    }

    // 🔵 bordo sottile
    private val strokePaint = Paint().apply {
        color = "#BFDBFE".toColorInt()
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }

    // ✍️ testo stile HUD
    private val textPaint = Paint().apply {
        color = "#FDE047".toColorInt()
        textSize = 34f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val radius = 65f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val index = event.actionIndex
        val pointerId = event.getPointerId(index)

        if (event.getY(index) < topExclusion)
            return false

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                touchPoints[pointerId] =
                    Pair(event.getX(index), event.getY(index))
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val id = event.getPointerId(i)
                    val x = event.getX(i)
                    val y = event.getY(i)

                    if (y >= topExclusion) {
                        touchPoints[id] = Pair(x, y)
                    }
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_CANCEL -> {
                touchPoints.remove(pointerId)
            }
        }

        onFingerChange?.invoke(touchPoints.size)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for ((_, p) in touchPoints) {
            val x = p.first
            val y = p.second

            // 🔥 LINEE TAGLIATE (precise)
            canvas.drawLine(x, 0f, x, y - radius, linePaint)
            canvas.drawLine(x, y + radius, x, height.toFloat(), linePaint)

            canvas.drawLine(0f, y, x - radius, y, linePaint)
            canvas.drawLine(x + radius, y, width.toFloat(), y, linePaint)

            // 🔥 glow sotto
            canvas.drawCircle(x, y, radius + 10f, glowPaint)

            // 🔵 cerchio principale
            canvas.drawCircle(x, y, radius, fillPaint)

            // 🔵 bordo
            canvas.drawCircle(x, y, radius, strokePaint)

            // ✍️ coordinate
            val text = "${x.toInt()}, ${y.toInt()}"
            canvas.drawText(text, x, y - radius - 20f, textPaint)
        }
    }
}