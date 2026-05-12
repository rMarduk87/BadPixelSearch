package rpt.tool.badpixelsearch.utils.view.drawing

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class DrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val RADIUS_SMALL = 15f
    private val RADIUS_LARGE = 40f

    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private data class DrawnCircle(val x: Float, val y: Float, val radius: Float)

    private val circles = mutableListOf<DrawnCircle>()

    private var fadeAnimator: ValueAnimator? = null
    private var currentAlpha = 255

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.alpha = currentAlpha

        for (circle in circles) {
            canvas.drawCircle(circle.x, circle.y, circle.radius, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val option = SharedPreferencesManager.drawingOption
        val currentRadius = if (option == 1 || option == 3) RADIUS_LARGE else RADIUS_SMALL

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                fadeAnimator?.cancel()
                currentAlpha = 255

                circles.add(DrawnCircle(event.x, event.y, currentRadius))
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {

                val historySize = event.historySize
                for (i in 0 until historySize) {
                    circles.add(DrawnCircle(
                        event.getHistoricalX(i),
                        event.getHistoricalY(i),
                        currentRadius
                    ))
                }
                circles.add(DrawnCircle(event.x, event.y, currentRadius))
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (option == 2 || option == 3) {
                    startFadeOutAnimation()
                }
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
                    circles.clear()
                    currentAlpha = 255
                    invalidate()
                }
            })
            start()
        }
    }
}