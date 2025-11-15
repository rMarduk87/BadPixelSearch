package rpt.tool.badpixelsearch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rpt.tool.badpixelsearch.databinding.ActivityTwoDtestBinding

class TwoDTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTwoDtestBinding

    private val handler = Handler(Looper.getMainLooper())
    private var lastFrameCount = 0

    private var lastDrawTime = 0L

    private val runnable = object : Runnable {
        override fun run() {

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

            // GPU Benchmark (basato su tempo di rendering)
            binding.txtGpu.text = buildString {
                append(getString(R.string.gpu_load))
                append(" ")
                append(binding.bouncingView.gpuLoad.toString())
                append(" %")
            }

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTwoDtestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}