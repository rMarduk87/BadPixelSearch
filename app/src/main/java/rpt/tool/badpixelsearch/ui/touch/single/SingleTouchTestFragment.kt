package rpt.tool.badpixelsearch.ui.touch.single

import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentSingleTouchTestBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import java.util.Locale

class SingleTouchTestFragment:
    BaseFragment<FragmentSingleTouchTestBinding>(FragmentSingleTouchTestBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.filled_0)
        )

        binding.drawGridView.columns = resources.getInteger(R.integer.single_touch_1_columns)
        binding.drawGridView.rows = resources.getInteger(R.integer.single_touch_1_rows)

        binding.drawGridView.onProgressChanged = { filledCount, totalCount ->
            val percentage = (filledCount.toDouble() / totalCount.toDouble()) * 100
            val formattedPercentage = String.format(Locale.getDefault(), "%.2f",
                percentage)
            binding.toolbar.menuTitle.text = buildString {
                append(getString(R.string.filled))
                append(" ")
                append(formattedPercentage)
                append("%")
            }
        }
    }
}