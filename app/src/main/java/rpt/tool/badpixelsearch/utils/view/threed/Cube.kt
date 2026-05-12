package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube(color: FloatArray) {

    private val vertexBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer
    private val program: Int
    private val drawColor: FloatArray = if (color.size == 3) {
        floatArrayOf(color[0], color[1], color[2], 1.0f)
    } else {
        color
    }

    // Cubetto più piccolo (lato 0.4 invece di 1.0)
    private val coords = floatArrayOf(
        -0.2f,  0.2f,  0.2f,   // top left front
        -0.2f, -0.2f,  0.2f,   // bottom left front
         0.2f, -0.2f,  0.2f,   // bottom right front
         0.2f,  0.2f,  0.2f,   // top right front
        -0.2f,  0.2f, -0.2f,   // top left back
        -0.2f, -0.2f, -0.2f,   // bottom left back
         0.2f, -0.2f, -0.2f,   // bottom right back
         0.2f,  0.2f, -0.2f    // top right back
    )

    private val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3, // front
        4, 5, 6, 4, 6, 7, // back
        4, 0, 3, 4, 3, 7, // top
        1, 5, 6, 1, 6, 2, // bottom
        4, 5, 1, 4, 1, 0, // left
        3, 2, 6, 3, 6, 7  // right
    )

    init {
        vertexBuffer = ByteBuffer.allocateDirect(coords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer.put(coords).position(0)

        drawListBuffer = ByteBuffer.allocateDirect(drawOrder.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
        drawListBuffer.put(drawOrder).position(0)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, """
            uniform mat4 uMVP;
            attribute vec4 vPosition;
            void main() {
                gl_Position = uMVP * vPosition;
            }
        """)

        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, """
            precision mediump float;
            uniform vec4 vColor;
            void main() {
                gl_FragColor = vColor;
            }
        """)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
    }

    fun draw(
        projection: FloatArray,
        view: FloatArray,
        offsetY: Float,
        angleX: Float,
        angleY: Float,
        scale: Float
    ) {
        GLES20.glUseProgram(program)

        val model = FloatArray(16)
        val mvp = FloatArray(16)

        Matrix.setIdentityM(model, 0)
        Matrix.translateM(model, 0, 0f, offsetY, 0f)
        Matrix.rotateM(model, 0, angleX, 1f, 0f, 0f)
        Matrix.rotateM(model, 0, angleY, 0f, 1f, 0f)
        Matrix.scaleM(model, 0, scale, scale, scale)

        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, view, 0, model, 0)
        Matrix.multiplyMM(mvp, 0, projection, 0, temp, 0)

        val pos = GLES20.glGetAttribLocation(program, "vPosition")
        val colorHandle = GLES20.glGetUniformLocation(program, "vColor")
        val mvpHandle = GLES20.glGetUniformLocation(program, "uMVP")

        GLES20.glEnableVertexAttribArray(pos)
        GLES20.glVertexAttribPointer(pos, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glUniform4fv(colorHandle, 1, drawColor, 0)
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvp, 0)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.size, GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        GLES20.glDisableVertexAttribArray(pos)
    }

    private fun loadShader(type: Int, code: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)
        return shader
    }
}