package rpt.tool.badpixelsearch.utils.view.drawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingLinesView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private data class Stroke(val path: Path, val color: Int)

    private val strokes = mutableListOf<Stroke>()
    private var currentPath: Path? = null
    private var currentColor = Color.GRAY

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (stroke in strokes) {
            paint.color = stroke.color
            canvas.drawPath(stroke.path, paint)
        }

        currentPath?.let {
            paint.color = currentColor
            canvas.drawPath(it, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path().apply {
                    moveTo(x, y)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let {
                    strokes.add(Stroke(it, currentColor))
                }
                currentPath = null
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun changeColor(newColor: Int) {
        currentColor = newColor
    }
}