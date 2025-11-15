package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class CubeMesh {

    // colore base #8ba9e9
    private val baseColor = floatArrayOf(139f/255f, 169f/255f, 233f/255f, 1f)

    // Vertici duplicati per avere normali faccia-per-faccia (24 vertici)
    private val vertexData = floatArrayOf(
        // X, Y, Z,    NX, NY, NZ
        // front face
        -1f, -1f,  1f,   0f, 0f, 1f,
        1f, -1f,  1f,   0f, 0f, 1f,
        1f,  1f,  1f,   0f, 0f, 1f,
        -1f,  1f,  1f,   0f, 0f, 1f,
        // right face
        1f, -1f,  1f,   1f, 0f, 0f,
        1f, -1f, -1f,   1f, 0f, 0f,
        1f,  1f, -1f,   1f, 0f, 0f,
        1f,  1f,  1f,   1f, 0f, 0f,
        // back face
        1f, -1f, -1f,   0f, 0f, -1f,
        -1f, -1f, -1f,   0f, 0f, -1f,
        -1f,  1f, -1f,   0f, 0f, -1f,
        1f,  1f, -1f,   0f, 0f, -1f,
        // left face
        -1f, -1f, -1f,  -1f, 0f, 0f,
        -1f, -1f,  1f,  -1f, 0f, 0f,
        -1f,  1f,  1f,  -1f, 0f, 0f,
        -1f,  1f, -1f,  -1f, 0f, 0f,
        // top face
        -1f,  1f,  1f,   0f, 1f, 0f,
        1f,  1f,  1f,   0f, 1f, 0f,
        1f,  1f, -1f,   0f, 1f, 0f,
        -1f,  1f, -1f,   0f, 1f, 0f,
        // bottom face
        -1f, -1f, -1f,   0f, -1f, 0f,
        1f, -1f, -1f,   0f, -1f, 0f,
        1f, -1f,  1f,   0f, -1f, 0f,
        -1f, -1f,  1f,   0f, -1f, 0f
    )

    // Indici (6 faces * 2 triangles * 3 indices = 36)
    private val indices = shortArrayOf(
        0,  1,  2,  0,  2,  3,    // front
        4,  5,  6,  4,  6,  7,    // right
        8,  9, 10,  8, 10, 11,    // back
        12, 13, 14, 12, 14, 15,    // left
        16, 17, 18, 16, 18, 19,    // top
        20, 21, 22, 20, 22, 23     // bottom
    )

    private val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .apply {
            put(vertexData)
            position(0)
        }
    private val indexBuffer: ShortBuffer = ByteBuffer.allocateDirect(indices.size * 2)
        .order(ByteOrder.nativeOrder())
        .asShortBuffer()
        .apply {
            put(indices)
            position(0)
        }

    private val program: Int

    private val vertexStride = 6 * 4 // 6 floats per vertex (pos3 + normal3) * 4 bytes

    init {

        val vs = """
            uniform mat4 uMVP;
            uniform mat4 uModel;
            attribute vec3 aPos;
            attribute vec3 aNormal;
            varying vec3 vNormal;
            varying vec3 vPos;
            void main() {
                vNormal = mat3(uModel) * aNormal; // trasformazione normali
                vPos = (uModel * vec4(aPos,1.0)).xyz;
                gl_Position = uMVP * vec4(aPos, 1.0);
            }
        """.trimIndent()

        val fs = """
            precision mediump float;
            uniform vec4 uColor;
            uniform vec3 uLightDir; // in world space (normalized)
            varying vec3 vNormal;
            varying vec3 vPos;
            void main() {
                vec3 N = normalize(vNormal);
                float diff = max(dot(N, normalize(uLightDir)), 0.0);
                float ambient = 0.25;
                float intensity = ambient + diff * 0.85;
                vec3 col = uColor.rgb * intensity;
                gl_FragColor = vec4(col, uColor.a);
            }
        """.trimIndent()

        val vsId = GLHelper.compileShader(GLES20.GL_VERTEX_SHADER, vs)
        val fsId = GLHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fs)
        program = GLES20.glCreateProgram().also { prog ->
            GLES20.glAttachShader(prog, vsId)
            GLES20.glAttachShader(prog, fsId)
            GLES20.glBindAttribLocation(prog, 0, "aPos")
            GLES20.glBindAttribLocation(prog, 1, "aNormal")
            GLES20.glLinkProgram(prog)
        }
    }

    fun draw(model: FloatArray, vp: FloatArray) {
        GLES20.glUseProgram(program)

        // calcola MVP
        val mvp = FloatArray(16)
        Matrix.multiplyMM(mvp, 0, vp, 0, model,
            0)

        val uMVP = GLES20.glGetUniformLocation(program, "uMVP")
        val uModel = GLES20.glGetUniformLocation(program, "uModel")
        val uColor = GLES20.glGetUniformLocation(program, "uColor")
        val uLightDir = GLES20.glGetUniformLocation(program, "uLightDir")

        GLES20.glUniformMatrix4fv(uMVP, 1, false, mvp, 0)
        GLES20.glUniformMatrix4fv(uModel, 1, false, model, 0)
        GLES20.glUniform4fv(uColor, 1, baseColor, 0)

        // luce direzionale (da sopra-destra-fronte)
        val light = floatArrayOf(0.3f, 0.7f, 0.6f)
        GLES20.glUniform3fv(uLightDir, 1, light, 0)

        // attributi
        val aPosLoc = GLES20.glGetAttribLocation(program, "aPos")
        val aNormLoc = GLES20.glGetAttribLocation(program, "aNormal")

        vertexBuffer.position(0)
        GLES20.glEnableVertexAttribArray(aPosLoc)
        GLES20.glVertexAttribPointer(aPosLoc, 3, GLES20.GL_FLOAT,
            false, vertexStride, vertexBuffer)

        vertexBuffer.position(3)
        GLES20.glEnableVertexAttribArray(aNormLoc)
        GLES20.glVertexAttribPointer(aNormLoc, 3, GLES20.GL_FLOAT,
            false, vertexStride, vertexBuffer)

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.size,
            GLES20.GL_UNSIGNED_SHORT, indexBuffer)

        GLES20.glDisableVertexAttribArray(aPosLoc)
        GLES20.glDisableVertexAttribArray(aNormLoc)
    }
}