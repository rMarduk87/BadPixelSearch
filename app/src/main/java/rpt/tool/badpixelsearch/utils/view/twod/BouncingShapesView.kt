package rpt.tool.badpixelsearch.utils.view.twod

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class BouncingShapesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // ========================================
    // PARAMETRI CONFIGURABILI
    // ========================================
    private val SHAPE_COUNT = 10
    private val SHAPE_SIZE = 120f
    private val SPEED = 10f
    private val SHAPE_CHANGE_INTERVAL = 15000L
    // ========================================

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private data class Shape(
        var x: Float,
        var y: Float,
        var size: Float,
        var dx: Float,
        var dy: Float,
        var color: Int
    )

    private val shapes = mutableListOf<Shape>()
    private var shapesAreCircles = false

    var frameCount = 0

    // Benchmark
    var cpuLoad = 0
    var gpuLoad = 0

    private var lastShapeTouchCheck = System.currentTimeMillis()

    init {
        generateShapes()
        measureCpuBaseline()
    }

    // -------------------------------
    // GENERA SHAPES RANDOM
    // -------------------------------
    private fun generateShapes() {
        for (i in 0 until SHAPE_COUNT) {
            shapes.add(
                Shape(
                    x = Random.nextInt(100, 800).toFloat(),
                    y = Random.nextInt(100, 1600).toFloat(),
                    size = SHAPE_SIZE,
                    dx = if (Random.nextBoolean()) SPEED else -SPEED,
                    dy = if (Random.nextBoolean()) SPEED else -SPEED,
                    color = Color.rgb(
                        Random.nextInt(60, 255),
                        Random.nextInt(60, 255),
                        Random.nextInt(60, 255)
                    )
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        frameCount++

        val startTime = System.nanoTime()

        updatePhysics()
        drawShapes(canvas)

        val drawTime = System.nanoTime() - startTime
        updateGpuLoad(drawTime)

        invalidate()
    }

    // -------------------------------
    // FISICA + CONTROLLO FORME
    // -------------------------------
    private fun updatePhysics() {
        val now = System.currentTimeMillis()

        var touchedWall = false

        for (shape in shapes) {
            shape.x += shape.dx
            shape.y += shape.dy

            if (shape.x <= 0 || shape.x + shape.size >= width) {
                shape.dx *= -1
                touchedWall = true
            }
            if (shape.y <= 0 || shape.y + shape.size >= height) {
                shape.dy *= -1
                touchedWall = true
            }
        }

        // Ogni 15 sec se una forma ha toccato una parete → cambia shape
        if (now - lastShapeTouchCheck > SHAPE_CHANGE_INTERVAL) {
            lastShapeTouchCheck = now
            if (touchedWall) shapesAreCircles = !shapesAreCircles
        }
    }

    // -------------------------------
    // DISEGNO FORME
    // -------------------------------
    private fun drawShapes(canvas: Canvas) {
        for (shape in shapes) {
            paint.color = shape.color

            if (shapesAreCircles) {
                canvas.drawCircle(
                    shape.x + shape.size / 2,
                    shape.y + shape.size / 2,
                    shape.size / 2, paint
                )
            } else {
                canvas.drawRect(
                    shape.x, shape.y,
                    shape.x + shape.size,
                    shape.y + shape.size, paint
                )
            }
        }
    }

    // -------------------------------
    // GPU BENCHMARK STIMATO
    // -------------------------------
    private fun updateGpuLoad(drawTimeNs: Long) {
        val ms = drawTimeNs / 1_000_000f
        gpuLoad = ((ms / 16f) * 100).toInt().coerceIn(0, 100)
    }

    // -------------------------------
    // CPU BENCHMARK STIMATO
    // -------------------------------
    private var cpuBaseline = 1_000_000L

    private fun measureCpuBaseline() {
        // piccolo calcolo intensivo
        val start = System.nanoTime()
        var x = 0L
        for (i in 0..600_000) x += i
        cpuBaseline = (System.nanoTime() - start)
    }

    private fun measureCpu() {
        val start = System.nanoTime()
        var x = 0L
        for (i in 0..600_000) x += (i * 2)
        val diff = System.nanoTime() - start

        cpuLoad = ((diff.toFloat() / cpuBaseline.toFloat()) * 100)
            .toInt()
            .coerceIn(0, 100)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // esegui benchmark CPU ogni 2s
        post(object : Runnable {
            override fun run() {
                measureCpu()
                postDelayed(this, 2000)
            }
        })
    }
}