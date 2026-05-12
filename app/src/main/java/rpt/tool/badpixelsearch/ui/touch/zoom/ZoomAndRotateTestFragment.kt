package rpt.tool.badpixelsearch.ui.touch.zoom

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentZoomAndRotateTestBinding
import rpt.tool.badpixelsearch.utils.view.threed.CubeTouchRenderer

class ZoomAndRotateTestFragment : BaseFragment<FragmentZoomAndRotateTestBinding>(
    FragmentZoomAndRotateTestBinding::inflate
) {

    private lateinit var renderer: CubeTouchRenderer
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private var scaleFactor = 1f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.touch_test_5)
        )

        renderer = CubeTouchRenderer()

        binding.glSurfaceView.apply {
            setEGLContextClientVersion(2)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

        setupTouch()
    }

    override fun onResume() {
        super.onResume()
        binding.glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.glSurfaceView.onPause()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouch() {
        scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = scaleFactor.coerceIn(0.5f, 3f)
                    renderer.setScale(scaleFactor)
                    return true
                }
            })

        binding.glSurfaceView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)

            if (event.pointerCount == 1) {
                renderer.handleDrag(event)
            }
            true
        }
    }
}