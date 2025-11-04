package rpt.tool.badpixelsearch

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rpt.tool.badpixelsearch.databinding.ActivityThreeDtestBinding
import rpt.tool.badpixelsearch.utils.view.threed.CubeRenderer
import rpt.tool.badpixelsearch.utils.view.threed.OpenGLHelper

class ThreeDTestActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var renderer: CubeRenderer
    private val testDurationMs = 5_000L // 5 seconds
    private val fpsThreshold = 25.0 // consideriamo sufficiente >= 25 fps
    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var binding: ActivityThreeDtestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThreeDtestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        glSurfaceView = GLSurfaceView(this)
        val glContainer = findViewById<android.view.ViewGroup>(R.id.glContainer)
        glContainer.addView(glSurfaceView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT)

        // Setup GLSurfaceView for OpenGL ES 2.0
        glSurfaceView.setEGLContextClientVersion(2)
        renderer = CubeRenderer()
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

        updateSystemInfo()

        binding.btnStartTest.setOnClickListener {
            binding.btnStartTest.isEnabled = false
            start3DTest()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateSystemInfo() {
        val req = OpenGLHelper.getReqGlEsVersion(this)
        val glStr = if (req == 0) "sconosciuta" else OpenGLHelper.glEsVersionString(req)
        binding.tvGLES.text = getString(R.string.opengl_es_request, glStr)
        val sensorsInfo = checkSensors()
        binding.tvSensors.text = getString(R.string._sensors, sensorsInfo)
    }

    private fun checkSensors(): String {
        val sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val hasGyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null
        val hasAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        val hasMag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null
        val parts = mutableListOf<String>()
        if (hasGyro) parts.add("Giroscopio")
        if (hasAccel) parts.add("Accelerometro")
        if (hasMag) parts.add("Magnetometro")
        return if (parts.isEmpty()) "Nessuno" else parts.joinToString(", ")
    }

    private fun start3DTest() {
        binding.tvInfo.text = getString(
            R.string.rendering_and_measuring_fps_per_seconds,
            testDurationMs / 1000
        )
        renderer.resetFpsMeasurement()

        // Start measurement coroutine
        mainScope.launch {
            delay(testDurationMs)
            // compute fps
            val fps = renderer.getAverageFps()
            binding.tvFpsResult.text = getString(R.string.fps_medio_1f).format(fps)
            val passed = fps >= fpsThreshold && OpenGLHelper.supportsGLES30(this@ThreeDTestActivity)
            if (passed) {
                binding.tvInfo.text = getString(R.string.test_passed)
            } else {
                binding.tvInfo.text =
                    getString(R.string.test_not_passed)
            }
            binding.btnStartTest.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
        mainScope.coroutineContext.cancelChildren()
    }
}