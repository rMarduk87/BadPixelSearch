package rpt.tool.badpixelsearch.utils.view.touch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class TouchView(context: Context) : View(context) {

    private val touchPoints = mutableMapOf<Int, Pair<Float, Float>>()

    var onFingerChange: ((Int) -> Unit)? = null

    private val linePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val circlePaint = Paint().apply {
        color = Color.BLUE
        alpha = 120
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.YELLOW
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                touchPoints[pointerId] =
                    Pair(event.getX(actionIndex), event.getY(actionIndex))
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val id = event.getPointerId(i)
                    touchPoints[id] = Pair(event.getX(i), event.getY(i))
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

        for ((_, point) in touchPoints) {
            val x = point.first
            val y = point.second

            canvas.drawLine(x, 0f, x, height.toFloat(), linePaint)
            canvas.drawLine(0f, y, width.toFloat(), y, linePaint)

            canvas.drawCircle(x, y, 60f, circlePaint)

            val text = "${x.toInt()}, ${y.toInt()}"
            canvas.drawText(text, x, y - 80f, textPaint)
        }
    }
}