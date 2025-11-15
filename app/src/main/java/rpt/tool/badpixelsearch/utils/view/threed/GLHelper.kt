package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import java.nio.*

object GLHelper {

    fun compileShader(type: Int, code: String): Int {
        val id = GLES20.glCreateShader(type)
        GLES20.glShaderSource(id, code)
        GLES20.glCompileShader(id)
        // opzionale: controllare compile status per debug
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            val log = GLES20.glGetShaderInfoLog(id)
            GLES20.glDeleteShader(id)
            throw RuntimeException("Shader compile error: $log")
        }
        return id
    }

    fun floatBufferOf(vararg values: Float): FloatBuffer =
        ByteBuffer.allocateDirect(values.size * 4).order(
            ByteOrder.nativeOrder()).asFloatBuffer().apply {
            put(values); position(0)
        }

    fun shortBufferOf(vararg values: Short): ShortBuffer =
        ByteBuffer.allocateDirect(values.size * 2).order(
            ByteOrder.nativeOrder()).asShortBuffer().apply {
            put(values); position(0)
        }
}