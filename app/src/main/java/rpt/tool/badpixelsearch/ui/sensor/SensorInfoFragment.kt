package rpt.tool.badpixelsearch.ui.sensor

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.MainActivity
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentSensorInfoBinding
import rpt.tool.badpixelsearch.utils.view.adapters.SensorAdapter

class SensorInfoFragment:
    BaseFragment<FragmentSensorInfoBinding>(FragmentSensorInfoBinding::inflate,false) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columns = resources.getInteger(R.integer.sensor_grid_column_count)
        binding.recyclerSensors.layoutManager = GridLayoutManager(requireContext(), columns)

        val sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        binding.recyclerSensors.adapter = SensorAdapter(sensors)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(rpt.tool.badpixelsearch.R.string.sensor_info_title))
        binding.toolbar.btnShare.visibility = View.VISIBLE

        binding.toolbar.btnShare.setOnClickListener {
            val infoList = sensors.map { sensor ->
                sensor.name to sensor.vendor
            }
            rpt.tool.badpixelsearch.utils.PdfUtils.shareInfoAsPdf(
                requireContext(),
                getString(rpt.tool.badpixelsearch.R.string.sensor_info_title),
                infoList
            )
        }

    }

    private fun finish() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }
}