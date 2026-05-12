package rpt.tool.badpixelsearch.utils.view.drawing

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class DrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val SIZE_SMALL = 30f
    private val SIZE_LARGE = 80f

    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private var paths = mutableListOf<Path>()
    private var currentPath = Path()
    private var fadeAnimator: ValueAnimator? = null
    private var currentAlpha = 255

    init {
        updatePaintSettings()
    }

    private fun updatePaintSettings() {
        val option = SharedPreferencesManager.drawingOption

        paint.strokeWidth = when (option) {
            0, 2 -> SIZE_SMALL
            1, 3 -> SIZE_LARGE
            else -> SIZE_SMALL
        }

        paint.alpha = 255
        currentAlpha = 255
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha = currentAlpha

        for (path in paths) {
            canvas.drawPath(path, paint)
        }
        canvas.drawPath(currentPath, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fadeAnimator?.cancel()
                updatePaintSettings()

                currentPath.reset()
                currentPath.moveTo(x, y)
                currentPath.lineTo(x + 0.1f, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                paths.add(currentPath)
                currentPath = Path()

                val option = SharedPreferencesManager.drawingOption
                if (option == 2 || option == 3) {
                    startFadeOutAnimation()
                }
                invalidate()
            }
        }
        return true
    }

    private fun startFadeOutAnimation() {
        fadeAnimator = ValueAnimator.ofInt(255, 0).apply {
            duration = 1500
            addUpdateListener { animator ->
                currentAlpha = animator.animatedValue as Int
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    paths.clear()
                    currentAlpha = 255
                    invalidate()
                }
            })
            start()
        }
    }
}