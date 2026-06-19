package rpt.tool.badpixelsearch.ui.drawing.color

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentDrawingColorLinesTestBinding

class DrawingColorLinesTestFragment : BaseFragment<FragmentDrawingColorLinesTestBinding>(
    FragmentDrawingColorLinesTestBinding::inflate,false
) {

    private val colors by lazy {
        listOf(
            ContextCompat.getColor(requireContext(), R.color.gray),
            ContextCompat.getColor(requireContext(), R.color.yellow),
            ContextCompat.getColor(requireContext(), R.color.cyan),
            ContextCompat.getColor(requireContext(), R.color.magenta),
            ContextCompat.getColor(requireContext(), R.color.red),
            ContextCompat.getColor(requireContext(), R.color.blue),
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.white)
        )
    }

    private val colorsName by lazy { resources.getStringArray(R.array.color_lines) }
    private var colorIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle,
            getString(rpt.tool.badpixelsearch.R.string.gray_lines))

        binding.toolbar.btnPlus.visibility = View.VISIBLE

        binding.drawingView.changeColor(colors[colorIndex])

        binding.toolbar.btnPlus.setOnClickListener {
            colorIndex = (colorIndex + 1) % colors.size
            binding.drawingView.changeColor(colors[colorIndex])
            binding.toolbar.menuTitle.text = colorsName[colorIndex]
        }
    }
}