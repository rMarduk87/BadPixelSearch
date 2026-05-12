package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import javax.microedition.khronos.opengles.GL10

class CubeTouchRenderer : GLSurfaceView.Renderer {

    private val cubes = mutableListOf<Cube>()

    private var angleX = 0f
    private var angleY = 0f
    private var scale = 1f

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceCreated(p0: GL10, p1: javax.microedition.khronos.egl.EGLConfig) {
        GLES20.glClearColor(0.02f, 0.02f, 0.09f, 1f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        if (cubes.isEmpty()) {
            // #9db7ce -> 157, 183, 206
            cubes.add(Cube(floatArrayOf(157/255f, 183/255f, 206/255f, 1f)))
            // #6c96bc -> 108, 150, 188
            cubes.add(Cube(floatArrayOf(108/255f, 150/255f, 188/255f, 1f)))
            // #396e9c -> 57, 110, 156
            cubes.add(Cube(floatArrayOf(57/255f, 110/255f, 156/255f, 1f)))
        }

        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, 6f,
            0f, 0f, 0f,
            0f, 1f, 0f
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 12f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Centro della pila spostato leggermente verso l'alto
        val verticalShift = 0.2f
        
        for ((i, cube) in cubes.withIndex()) {
            cube.draw(
                projectionMatrix,
                viewMatrix,
                ((1 - i) * 1.0f) + verticalShift, // Spaziatura 1.0 e shift verso l'alto
                angleX,
                angleY,
                scale
            )
        }
    }

    private var lastX = 0f
    private var lastY = 0f

    fun handleDrag(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastX
                val dy = event.y - lastY

                angleY += dx * 0.3f
                angleX += dy * 0.3f

                lastX = event.x
                lastY = event.y
            }
        }
    }

    fun setScale(s: Float) {
        scale = s
    }
}