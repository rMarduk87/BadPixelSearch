package rpt.tool.badpixelsearch.utils.view.physics

import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.View
import kotlin.math.sqrt

class GravityBallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs), SensorEventListener {

    /* ================= SENSORI ================= */

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // direzione della gravità (normalizzata)
    private var dirX = 0f
    private var dirY = 1f   // default: verso il basso

    /* ================= PAINT ================= */

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF8E24AA.toInt()
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val ballPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    /* ================= GRIGLIA ================= */

    private val gridCount = 4

    /* ================= FISICA ================= */

    private val gravity = 0.9f          // accelerazione costante
    private val restitution = 0.88f     // elasticità rimbalzo
    private val damping = 0.995f        // smorzamento minimo

    private var radius = 80f

    private var x = 0f
    private var y = 0f
    private var vx = 0f
    private var vy = 0f

    private var initialized = false

    /* ================= LIFECYCLE ================= */

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onDetachedFromWindow() {
        sensorManager.unregisterListener(this)
        super.onDetachedFromWindow()
    }

    /* ================= SENSOR ================= */

    override fun onSensorChanged(event: SensorEvent) {
        val ax = -event.values[0]
        val ay = event.values[1]

        val len = sqrt(ax * ax + ay * ay).coerceAtLeast(1f)

        dirX = ax / len
        dirY = ay / len
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    /* ================= DRAW LOOP ================= */

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!initialized && width > 0 && height > 0) {
            x = width / 2f
            y = height / 2f
            vx = 0f
            vy = 0f
            initialized = true
        }

        drawGrid(canvas)
        updatePhysics()
        drawBall(canvas)

        postInvalidateOnAnimation()
    }

    /* ================= DRAW ================= */

    private fun drawGrid(canvas: Canvas) {
        val cellW = width / gridCount.toFloat()
        val cellH = height / gridCount.toFloat()

        for (i in 0..gridCount) {
            val gx = i * cellW
            canvas.drawLine(gx, 0f, gx, height.toFloat(), gridPaint)
        }

        for (i in 0..gridCount) {
            val gy = i * cellH
            canvas.drawLine(0f, gy, width.toFloat(), gy, gridPaint)
        }
    }

    private fun drawBall(canvas: Canvas) {

        val shader = RadialGradient(
            x, y, radius,
            intArrayOf(
                0xFFD1A3FF.toInt(), // centro chiaro
                0xFF9C27B0.toInt(),
                0xFF4A0072.toInt()  // bordo scuro
            ),
            floatArrayOf(0f, 0.6f, 1f),
            Shader.TileMode.CLAMP
        )

        ballPaint.shader = shader
        canvas.drawCircle(x, y, radius, ballPaint)

        // puntino bianco centrale
        canvas.drawCircle(x, y, 6f, dotPaint)
    }

    /* ================= PHYSICS ================= */

    private fun updatePhysics() {

        // gravità costante, direzione data dall'accelerometro
        vx += dirX * gravity
        vy += dirY * gravity

        vx *= damping
        vy *= damping

        x += vx
        y += vy

        if (x - radius < 0) {
            x = radius
            vx = -vx * restitution
        }

        if (x + radius > width) {
            x = width - radius
            vx = -vx * restitution
        }

        if (y - radius < 0) {
            y = radius
            vy = -vy * restitution
        }

        if (y + radius > height) {
            y = height - radius
            vy = -vy * restitution
        }
    }
}