package rpt.tool.badpixelsearch.ui.color.line

import android.graphics.Color
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentColorLineTestBinding

class ColorLineTestFragment :
    BaseFragment<FragmentColorLineTestBinding>(FragmentColorLineTestBinding::inflate) {

    private val colors = listOf(
        Color.WHITE,
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.MAGENTA,
        Color.CYAN,
        Color.YELLOW
    )

    private var names = listOf<String>()

    private var index = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        names = listOf(
            requireContext().resources.getString(R.string.white_lines),
            requireContext().resources.getString(R.string.red_lines),
            requireContext().resources.getString(R.string.green_lines),
            requireContext().resources.getString(R.string.blue_lines),
            requireContext().resources.getString(R.string.magenta_lines),
            requireContext().resources.getString(R.string.cyan_lines),
            requireContext().resources.getString(R.string.yellow_lines))

        applyColor()

        binding.rootLayout.setOnClickListener {
            index = (index + 1) % colors.size
            applyColor()
        }
    }

    private fun applyColor() {
        val c = colors[index]
        binding.gridView.gridColor = c
        binding.txtColorName.text = names[index]
    }
}