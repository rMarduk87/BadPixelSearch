package rpt.tool.badpixelsearch.ui.animation.threed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentThreeDTestBinding

class ThreeDTestFragment : BaseFragment<FragmentThreeDTestBinding>(FragmentThreeDTestBinding::inflate) {

    private val handler = Handler(Looper.getMainLooper())
    private var lastFrameCount = 0

    private val fpsUpdater = object : Runnable {
        override fun run() {
            if (view == null) return
            val renderer = binding.glView.renderer
            val fps = renderer.frameCount - lastFrameCount
            lastFrameCount = renderer.frameCount
            binding.txtFps3d.text = buildString {
                append(getString(R.string.fps))
                append(" ")
                append(fps.toString())
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(R.string.threed))
        handler.post(fpsUpdater)
    }

    override fun onPause() {
        super.onPause()
        binding.glView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.glView.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(fpsUpdater)
    }
}