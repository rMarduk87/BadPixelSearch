package rpt.tool.badpixelsearch.utils.view.cell

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GridLinesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = 0xFFFFFFFF.toInt()
    }

    private val innerRectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = 0xFFFFFFFF.toInt()
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0xFFFFFFFF.toInt()
    }

    var gridColor: Int = 0xFFFFFFFF.toInt()
        set(value) {
            field = value
            linePaint.color = value
            innerRectPaint.color = value
            dotPaint.color = value
            invalidate()
        }

    private val cols = 10
    private val rows = 14

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(0xFF000000.toInt())

        val cellW = width / cols.toFloat()
        val cellH = height / rows.toFloat()

        for (r in 0 until rows) {
            for (c in 0 until cols) {

                val left = c * cellW
                val top = r * cellH
                val right = left + cellW
                val bottom = top + cellH

                canvas.drawRect(left, top, right, bottom, linePaint)

                val pad = cellW * 0.15f
                canvas.drawRect(
                    left + pad,
                    top + pad,
                    right - pad,
                    bottom - pad,
                    innerRectPaint
                )

                canvas.drawCircle(
                    left + cellW / 2f,
                    top + cellH / 2f,
                    cellW * 0.03f,
                    dotPaint
                )
            }
        }
    }
}