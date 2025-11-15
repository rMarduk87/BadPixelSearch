package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import kotlin.math.sin
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CubeRenderer : GLSurfaceView.Renderer {

    private lateinit var cube: CubeMesh

    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val vpMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    // animazione
    private var time = 0f

    // per FPS
    var frameCount = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        cube = CubeMesh()
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, 6.0f,   // camera più distante per cubo grande
            0f, 0f, 0f,
            0f, 1f, 0f
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio,
            1f, 50f)
    }

    override fun onDrawFrame(gl: GL10?) {
        frameCount++
        time += 0.033f // avanzamento temporale (≈30Hz increment) -> il dispositivo regolerà FPS reali

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // movimento verticale oscillante (leggero)
        val upDown = sin(time * 0.8f) * 0.6f

        // rotazioni multiple per effetto "filmato"
        val rotX = time * 35f
        val rotY = time * 60f
        val rotZ = time * 18f

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, 0.5f, 0.5f, 0.5f) // riduce la dimensione alla metà
        Matrix.translateM(modelMatrix, 0, 0f, upDown, 0f)
        Matrix.rotateM(modelMatrix, 0, rotX, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotY, 0f, 1f, 0f)
        Matrix.rotateM(modelMatrix, 0, rotZ, 0f, 0f, 1f)

        // VP
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix,
            0, viewMatrix, 0)

        cube.draw(modelMatrix, vpMatrix)
    }
}