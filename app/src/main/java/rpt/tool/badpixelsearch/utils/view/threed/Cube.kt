package rpt.tool.badpixelsearch.utils.view.threed

import android.opengl.GLES20
import android.opengl.Matrix

class Cube(private val color: FloatArray) {

    fun draw(offsetY: Float, angleX: Float, angleY: Float, scale: Float) {

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        val matrix = FloatArray(16)

        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, 0f, offsetY, -6f)
        Matrix.rotateM(matrix, 0, angleX, 1f, 0f, 0f)
        Matrix.rotateM(matrix, 0, angleY, 0f, 1f, 0f)
        Matrix.scaleM(matrix, 0, scale, scale, scale)

    }
}