package rpt.tool.badpixelsearch.utils.view.drawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import androidx.core.graphics.toColorInt

class DrawGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var columns = 20
    var rows = 40

    private var cellSize = 0f
    private var offsetX = 0f
    private var offsetY = 0f

    private var gridState = Array(columns) { BooleanArray(rows) }

    private val paintDark = Paint(Paint.ANTI_ALIAS_FLAG).apply { color =
        "#0B132B".toColorInt() }
    private val paintLight = Paint(Paint.ANTI_ALIAS_FLAG).apply { color =
        "#2A52BE".toColorInt() }
    private val rect = RectF()
    private val padding = 4f
    private val cornerRadius = 8f

    var onProgressChanged: ((filledCount: Int, totalCount: Int) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val maxCellWidth = w.toFloat() / columns
        val maxCellHeight = h.toFloat() / rows

        cellSize = min(maxCellWidth, maxCellHeight)

        val totalGridWidth = cellSize * columns
        val totalGridHeight = cellSize * rows
        offsetX = (w - totalGridWidth) / 2f
        offsetY = (h - totalGridHeight) / 2f

        gridState = Array(columns) { BooleanArray(rows) }
        calculateProgress()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until columns) {
            for (j in 0 until rows) {
                val left = offsetX + (i * cellSize)
                val top = offsetY + (j * cellSize)

                rect.set(left + padding, top + padding, left + cellSize - padding,
                    top + cellSize - padding)

                val paint = if (gridState[i][j]) paintLight else paintDark
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var needsRedraw = false

        for (i in 0 until event.pointerCount) {
            val x = event.getX(i) - offsetX
            val y = event.getY(i) - offsetY

            val col = (x / cellSize).toInt()
            val row = (y / cellSize).toInt()

            if (x >= 0 && y >= 0 && col in 0 until columns && row in 0 until rows) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (!gridState[col][row]) {
                            gridState[col][row] = true
                            needsRedraw = true
                        }
                    }
                }
            }
        }

        if (needsRedraw) {
            invalidate()
            calculateProgress()
        }

        return true
    }

    private fun calculateProgress() {
        var filled = 0
        for (i in 0 until columns) {
            for (j in 0 until rows) {
                if (gridState[i][j]) filled++
            }
        }
        onProgressChanged?.invoke(filled, columns * rows)
    }
}