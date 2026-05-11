package rpt.tool.badpixelsearch.utils.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rpt.tool.badpixelsearch.R

class GridAdapter(
    private val totalItems: Int,
    private val onProgressChanged: (Int) -> Unit
) : RecyclerView.Adapter<GridAdapter.SquareViewHolder>() {

    private val filledSquares = mutableSetOf<Int>()

    inner class SquareViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val squareView: View = view.findViewById(R.id.squareView)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (filledSquares.contains(position)) {
                        filledSquares.remove(position)
                    } else {
                        filledSquares.add(position)
                    }
                    notifyItemChanged(position)
                    onProgressChanged(filledSquares.size)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquareViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_square, parent, false)
        return SquareViewHolder(view)
    }

    override fun onBindViewHolder(holder: SquareViewHolder, position: Int) {
        if (filledSquares.contains(position)) {
            holder.squareView.setBackgroundResource(R.drawable.bg_square_light)
        } else {
            holder.squareView.setBackgroundResource(R.drawable.bg_square_dark)
        }
    }

    override fun getItemCount(): Int = totalItems
}