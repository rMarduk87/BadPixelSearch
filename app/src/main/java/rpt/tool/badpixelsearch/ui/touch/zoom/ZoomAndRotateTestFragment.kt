package rpt.tool.badpixelsearch.ui.touch.zoom

import android.annotation.SuppressLint
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

    private var scaleFactor = 1.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.touch_test_5)
        )

        renderer = CubeTouchRenderer()

        binding.glSurfaceView.setEGLContextClientVersion(2)
        binding.glSurfaceView.setRenderer(renderer)

        setupTouch()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouch() {
        scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)
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