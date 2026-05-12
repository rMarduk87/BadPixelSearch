package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.EGLConfig
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import javax.microedition.khronos.opengles.GL10

class CubeTouchRenderer : GLSurfaceView.Renderer {

    private val cubes = mutableListOf<Cube>()

    private var angleX = 0f
    private var angleY = 0f
    private var scale = 1f

    override fun onSurfaceCreated(p0: GL10, p1: javax.microedition.khronos.egl.EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        cubes.add(Cube(floatArrayOf(1f, 0f, 0f)))
        cubes.add(Cube(floatArrayOf(0f, 1f, 0f)))
        cubes.add(Cube(floatArrayOf(0f, 0f, 1f)))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        for ((index, cube) in cubes.withIndex()) {
            cube.draw(
                offsetY = (index - 1) * 2.5f,
                angleX = angleX,
                angleY = angleY,
                scale = scale
            )
        }
    }

    fun handleDrag(event: MotionEvent) {
        angleX += event.x / 100
        angleY += event.y / 100
    }

    fun setScale(s: Float) {
        scale = s
    }
}