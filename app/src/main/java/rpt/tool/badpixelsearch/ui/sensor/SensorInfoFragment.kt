package rpt.tool.badpixelsearch.ui.sensor

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.MainActivity
import rpt.tool.badpixelsearch.databinding.FragmentSensorInfoBinding
import rpt.tool.badpixelsearch.utils.view.adapters.SensorAdapter

class SensorInfoFragment:
    BaseFragment<FragmentSensorInfoBinding>(FragmentSensorInfoBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerSensors.layoutManager = LinearLayoutManager(requireContext())

        val sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        binding.recyclerSensors.adapter = SensorAdapter(sensors)

        binding.leftIconBlock.setOnClickListener{ finish() }

    }

    private fun finish() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }
}