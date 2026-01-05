package rpt.tool.badpixelsearch.utils.view.physics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import kotlin.math.sin

class Gravity3DBallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // ===== CONFIG =====
    private val gridCols = 3
    private val gridRows = 3

    private val baseRadius = 200f
    private val bounce = 0.86f
    private val frameDelay = 16L

    private val gridColor = "#262640".toColorInt()
    private val ballColor = "#52638d".toColorInt()

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gridColor
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    private val ballPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var x = 0f
    private var y = 0f
    private var velX = 0f
    private var velY = 0f
    private var gravityX = 0f
    private var gravityY = 1.5f

    private var zPhase = 0f
    private var zAmplitude = 0f
    private var scaleZ = 1f

    private val gridRect = RectF()

    init {
        startLoop()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val gridWidth = w * 0.75f
        val gridHeight = h * 0.6f

        val left = (w - gridWidth) / 2f
        val top = (h - gridHeight) / 2f

        gridRect.set(left, top, left + gridWidth, top + gridHeight)

        x = gridRect.centerX()
        y = gridRect.top + baseRadius
    }

    fun updateGravity(gx: Float, gy: Float) {
        gravityX = gx * 0.22f
        gravityY = gy * 0.35f
    }

    private fun startLoop() {
        postDelayed(object : Runnable {
            override fun run() {
                updatePhysics()
                invalidate()
                postDelayed(this, frameDelay)
            }
        }, frameDelay)
    }

    private fun updatePhysics() {
        velX += gravityX
        velY += gravityY

        x += velX
        y += velY

        var bounced = false

        if (x - baseRadius < gridRect.left) {
            x = gridRect.left + baseRadius
            velX = -velX * bounce
            bounced = true
        }

        if (x + baseRadius > gridRect.right) {
            x = gridRect.right - baseRadius
            velX = -velX * bounce
            bounced = true
        }

        if (y - baseRadius < gridRect.top) {
            y = gridRect.top + baseRadius
            velY = -velY * bounce
            bounced = true
        }

        if (y + baseRadius > gridRect.bottom) {
            y = gridRect.bottom - baseRadius
            velY = -velY * bounce
            bounced = true
        }

        if (bounced) {
            zAmplitude = 1f
        }

        zPhase += 0.12f
        zAmplitude *= 0.96f

        scaleZ = 1f + sin(zPhase) * 0.08f * zAmplitude

        velX *= 0.995f
        velY *= 0.995f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
        drawBall(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val cellW = gridRect.width() / gridCols
        val cellH = gridRect.height() / gridRows

        canvas.drawRect(gridRect, gridPaint)

        for (i in 1 until gridCols) {
            canvas.drawLine(
                gridRect.left + i * cellW,
                gridRect.top,
                gridRect.left + i * cellW,
                gridRect.bottom,
                gridPaint
            )
        }

        for (i in 1 until gridRows) {
            canvas.drawLine(
                gridRect.left,
                gridRect.top + i * cellH,
                gridRect.right,
                gridRect.top + i * cellH,
                gridPaint
            )
        }
    }

    private fun drawBall(canvas: Canvas) {
        val radius = baseRadius * scaleZ

        val shader = RadialGradient(
            x - radius / 3,
            y - radius / 3,
            radius * 1.3f,
            intArrayOf(
                Color.WHITE,
                ballColor,
                Color.rgb(
                    (Color.red(ballColor) * 0.6f).toInt(),
                    (Color.green(ballColor) * 0.6f).toInt(),
                    (Color.blue(ballColor) * 0.6f).toInt()
                )
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )

        ballPaint.shader = shader
        canvas.drawCircle(x, y, radius, ballPaint)

        canvas.drawCircle(
            x - radius / 3,
            y - radius / 3,
            radius / 6,
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                alpha = 90
            }
        )
    }
}