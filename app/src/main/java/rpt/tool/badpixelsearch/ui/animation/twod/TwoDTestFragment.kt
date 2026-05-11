package rpt.tool.badpixelsearch.ui.animation.twod

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentTwoDTestBinding

class TwoDTestFragment : BaseFragment<FragmentTwoDTestBinding>(FragmentTwoDTestBinding::inflate) {

    private val handler = Handler(Looper.getMainLooper())
    private var lastFrameCount = 0

    private val runnable = object : Runnable {
        override fun run() {
            if (view == null) return

            // FPS
            val fps = binding.bouncingView.frameCount - lastFrameCount
            lastFrameCount = binding.bouncingView.frameCount
            binding.txtFps.text = buildString {
                append(getString(R.string.fps))
                append(" ")
                append(fps.toString())
            }

            // CPU Benchmark
            val cpuLoad = binding.bouncingView.cpuLoad
            binding.txtCpu.text = buildString {
                append(getString(R.string.cpu_test))
                append(" ")
                append(cpuLoad.toString())
                append(" %")
            }

            // GPU Benchmark
            binding.txtGpu.text = buildString {
                append(getString(R.string.gpu_load))
                append(" ")
                append(binding.bouncingView.gpuLoad.toString())
                append(" %")
            }

            handler.postDelayed(this, 1000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(R.string.two))
        handler.post(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}