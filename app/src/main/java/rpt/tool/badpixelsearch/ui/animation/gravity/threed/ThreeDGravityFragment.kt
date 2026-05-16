package rpt.tool.badpixelsearch.ui.animation.gravity.threed

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentThreedGravityBinding
import java.util.Locale
import java.util.Locale.getDefault

class ThreeDGravityFragment :
    BaseFragment<FragmentThreedGravityBinding>(FragmentThreedGravityBinding::inflate),
    SensorEventListener {

    private lateinit var sensorManager: SensorManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string._3d_gravity).uppercase(getDefault())
        )

        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val gx = -event.values[0]
        val gy = event.values[1]
        binding.gravityView.updateGravity(gx, gy)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}