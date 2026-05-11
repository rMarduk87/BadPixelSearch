package rpt.tool.badpixelsearch.ui.touch.single

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentSingleTouchTestBinding
import rpt.tool.badpixelsearch.utils.view.adapters.GridAdapter
import java.util.Locale

class SingleTouchTestFragment:
    BaseFragment<FragmentSingleTouchTestBinding>(FragmentSingleTouchTestBinding::inflate) {

    private val columns = 10
    private val rows = 40
    private val totalSquares = columns * rows

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.filled_0)
        )

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),
            columns)

        val adapter = GridAdapter(totalSquares) { filledCount ->
            val percentage = (filledCount.toDouble() / totalSquares.toDouble()) * 100

            val formattedPercentage = String.format(
                Locale.getDefault(), "%.2f",
                percentage
            )

            binding.toolbar.menuTitle.text =
                getString(R.string.filled, formattedPercentage)
        }

        binding.recyclerView.adapter = adapter
    }
}