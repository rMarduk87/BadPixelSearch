package rpt.tool.badpixelsearch.ui.animation.gravity.threed

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.FragmentThreedGravityBinding

class ThreeDGravityFragment :
    BaseFragment<FragmentThreedGravityBinding>(FragmentThreedGravityBinding::inflate),
    SensorEventListener {

    private lateinit var sensorManager: SensorManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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