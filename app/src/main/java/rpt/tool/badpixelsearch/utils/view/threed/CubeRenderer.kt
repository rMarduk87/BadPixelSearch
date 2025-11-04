package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max

class CubeRenderer : GLSurfaceView.Renderer {

    // Simple cube vertices and colors (positions + colors)
    private val cubeCoords = floatArrayOf(
        // positions         // colors
        -1f, 1f, 1f, 1f, 0f, 0f,
        -1f, -1f, 1f, 0f, 1f, 0f,
        1f, -1f, 1f, 0f, 0f, 1f,
        1f, 1f, 1f, 1f, 1f, 0f,
        -1f, 1f, -1f, 1f, 0f, 1f,
        -1f, -1f, -1f, 0f, 1f, 1f,
        1f, -1f, -1f, 1f, 1f, 1f,
        1f, 1f, -1f, 0.5f, 0.5f, 0.5f
    )

    private val indexOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3,     // front
        4, 5, 6, 4, 6, 7,     // back
        4, 5, 1, 4, 1, 0,     // left
        3, 2, 6, 3, 6, 7,     // right
        4, 0, 3, 4, 3, 7,     // top
        1, 5, 6, 1, 6, 2      // bottom
    )

    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var indexBuffer: ByteBuffer

    private var program = 0

    // matrices
    private val mvpMatrix = FloatArray(16)
    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    // timing / fps measurement
    private val frameCount = AtomicInteger(0)
    private val startTimeMs = AtomicLong(0)
    private var lastTimeMs = System.currentTimeMillis()

    private var angle = 0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // init buffers
        vertexBuffer = ByteBuffer.allocateDirect(cubeCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexBuffer.put(cubeCoords).position(0)

        indexBuffer = ByteBuffer.allocateDirect(indexOrder.size * 2)
            .order(ByteOrder.nativeOrder())
        for (s in indexOrder) {
            indexBuffer.putShort(s)
        }
        indexBuffer.position(0)

        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        program = createSimpleProgram()
        resetFpsMeasurement()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height.toFloat()
        Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 10f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // camera
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, 5f,    // eye
            0f, 0f, 0f,    // center
            0f, 1f, 0f
        )    // up

        Matrix.setIdentityM(modelMatrix, 0)
        angle += 0.5f
        Matrix.rotateM(modelMatrix, 0, angle, 1f, 1f, 0f)

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvpMatrix, 0)

        // draw cube (simple, interleaved array: pos (3) + color (3))
        GLES20.glUseProgram(program)
        val stride = 6 * 4

        val posHandle = GLES20.glGetAttribLocation(program, "aPosition")
        val colorHandle = GLES20.glGetAttribLocation(program, "aColor")
        val mvpHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(posHandle, 3, GLES20.GL_FLOAT, false, stride, vertexBuffer)
        GLES20.glEnableVertexAttribArray(posHandle)

        vertexBuffer.position(3)
        GLES20.glVertexAttribPointer(colorHandle, 3, GLES20.GL_FLOAT, false, stride, vertexBuffer)
        GLES20.glEnableVertexAttribArray(colorHandle)

        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indexOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )

        // fps measurement
        frameCount.incrementAndGet()
        if (startTimeMs.get() == 0L) startTimeMs.set(System.currentTimeMillis())
        lastTimeMs = System.currentTimeMillis()
    }

    private fun createSimpleProgram(): Int {
        val vertexShaderCode = """
            attribute vec3 aPosition;
            attribute vec3 aColor;
            varying vec3 vColor;
            uniform mat4 uMVPMatrix;
            void main(){
                vColor = aColor;
                gl_Position = uMVPMatrix * vec4(aPosition,1.0);
            }
        """.trimIndent()

        val fragmentShaderCode = """
            precision mediump float;
            varying vec3 vColor;
            void main(){
                gl_FragColor = vec4(vColor, 1.0);
            }
        """.trimIndent()

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        val prog = GLES20.glCreateProgram()
        GLES20.glAttachShader(prog, vertexShader)
        GLES20.glAttachShader(prog, fragShader)
        GLES20.glLinkProgram(prog)
        return prog
    }

    private fun loadShader(type: Int, code: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)
        return shader
    }

    // FPS helpers
    fun resetFpsMeasurement() {
        frameCount.set(0)
        startTimeMs.set(0)
    }

    fun getAverageFps(): Double {
        val start = startTimeMs.get()
        val frames = frameCount.get()
        val end = lastTimeMs
        val durationSec = if (start == 0L) 0.0001 else max(1, (end - start).toInt()) / 1000.0
        return if (durationSec <= 0) 0.0 else frames / durationSec
    }
}