package rpt.tool.badpixelsearch.utils.view.adapters

import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rpt.tool.badpixelsearch.R

class SensorAdapter(
    private val sensors: List<Sensor>
) : RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    inner class SensorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtSensorName)
        val vendor: TextView = view.findViewById(R.id.txtSensorVendor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor, parent, false)
        return SensorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensor = sensors[position]
        holder.name.text = holder.itemView.context.getString(R.string.sensor_name) + " " +
                sensor.name
        holder.vendor.text = holder.itemView.context.getString(R.string.vendor) + " " +
                sensor.vendor
    }

    override fun getItemCount(): Int = sensors.size
}