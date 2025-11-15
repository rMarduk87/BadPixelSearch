package rpt.tool.badpixelsearch


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import rpt.tool.badpixelsearch.databinding.ActivityThreeDtestBinding


class ThreeDTestActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var lastFrameCount = 0
    private lateinit var binding: ActivityThreeDtestBinding

    private val fpsUpdater = object : Runnable {
        override fun run() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThreeDtestBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


}