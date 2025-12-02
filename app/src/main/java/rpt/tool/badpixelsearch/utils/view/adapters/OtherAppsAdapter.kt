package rpt.tool.badpixelsearch.utils.view.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.utils.data.OtherApps


class OtherAppsAdapter(
    var mContext: Context, other: ArrayList<OtherApps>,
    var callBack: CallBack
) :
    RecyclerView.Adapter<OtherAppsAdapter.ViewHolder?>() {
    private val otherArrayList: ArrayList<OtherApps> = other

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return otherArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_item_other_apps, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.app_name.text = otherArrayList[position].name

        holder.divider.visibility = View.VISIBLE

        Glide.with(mContext).load(getImage(position)).into(holder.imageView)

        holder.item_block.setOnClickListener {
            callBack.onClickSelect(
                otherArrayList[position],
                position
            )
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView =
            itemView.findViewById<ImageView>(R.id.app_img)
        var item_block: LinearLayout = itemView.findViewById<LinearLayout>(R.id.item_block)
        var app_name: TextView = itemView.findViewById<TextView>(R.id.app_name)
        var divider: View = itemView.findViewById<View>(R.id.divider)
        var super_item_block: RelativeLayout =
            itemView.findViewById<RelativeLayout>(R.id.super_item_block)

    }

    interface CallBack {
        fun onClickSelect(other: OtherApps, position: Int)
    }

    private fun getImage(pos: Int): Int {
        var drawable: Int = R.drawable.ic_pong_clock

        val `val`: String = otherArrayList[pos].id.toString()

        if (`val`.toInt() == 0) drawable = R.drawable.ic_pong_clock
        else if (`val`.toInt() == 1) drawable = R.drawable.ic_marimo_care

        return drawable
    }
}