package rpt.tool.badpixelsearch.utils.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GridCanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val gridPaint = Paint().apply {
        strokeWidth = 0.0025f // Line width
        color = Color.WHITE // Initial grid color
    }
    private val bgPaint = Paint().apply {
        color = Color.BLACK // Solid black background
    }

    private var gridColor = Color.WHITE
    private val squareSizePx = 0.05f // Each square is 2px

    // Colors to cycle through when clicked
    private val colors = listOf(
        Color.WHITE,
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.YELLOW,
        Color.CYAN,
        Color.MAGENTA
    )
    private var currentColorIndex = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw black background first
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Draw grid lines
        drawGrid(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        // Draw vertical lines
        var x = 0f
        while (x < width) {
            canvas.drawLine(x, 0f, x, height, gridPaint)
            x += squareSizePx
        }

        // Draw horizontal lines
        var y = 0f
        while (y < height) {
            canvas.drawLine(0f, y, width, y, gridPaint)
            y += squareSizePx
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Change to next color
            currentColorIndex = (currentColorIndex + 1) % colors.size
            gridColor = colors[currentColorIndex]
            gridPaint.color = gridColor
            invalidate() // Redraw with new color
            return true
        }
        return super.onTouchEvent(event)
    }
}