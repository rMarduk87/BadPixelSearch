package rpt.tool.badpixelsearch.utils.view.threed

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class Cube3DView(context: Context, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {

    val renderer: CubeRenderer

    constructor(context: Context) : this(context, null)

    init {
        setEGLContextClientVersion(2)
        renderer = CubeRenderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}