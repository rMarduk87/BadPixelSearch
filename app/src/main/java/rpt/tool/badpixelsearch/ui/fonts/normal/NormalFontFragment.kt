package rpt.tool.badpixelsearch.ui.fonts.normal

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentNormalFontBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class NormalFontFragment :
    BaseFragment<FragmentNormalFontBinding>(FragmentNormalFontBinding::inflate) {

    private val colors by lazy {
        listOf(
            ContextCompat.getColor(requireContext(), R.color.gray),
            ContextCompat.getColor(requireContext(), R.color.white),
            ContextCompat.getColor(requireContext(), R.color.red),
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.blue),
            ContextCompat.getColor(requireContext(), R.color.magenta),
            ContextCompat.getColor(requireContext(), R.color.cyan),
            ContextCompat.getColor(requireContext(), R.color.yellow)
        )
    }

    private val colorsText = listOf(
        R.string.gray_text,
        R.string.white_text,
        R.string.red_text,
        R.string.green_text,
        R.string.blue_text,
        R.string.magenta_text,
        R.string.cyan_text,
        R.string.yellow_text
    )

    private var colorIndex = 0

    private val titleIds = setOf(
        R.id.title_xsmall,
        R.id.title_small,
        R.id.title_medium,
        R.id.title_large
    )

    private val textLines = mutableListOf<TextView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(colorsText[colorIndex])
        )

        findAllTextViews(view)

        val initial = colors[0]
        textLines.forEach { it.setTextColor(initial) }

        textLines.forEach { line ->

            if(SharedPreferencesManager.IsBold){
                line.setTypeface(line.typeface, Typeface.BOLD)
            }
            else{
                line.setTypeface(line.typeface, Typeface.NORMAL)
            }

            line.setOnClickListener {
                changeAllColors(line)
            }
        }
    }

    private fun findAllTextViews(root: View) {
        if (root is TextView && root.id !in titleIds && root.id != R.id.txtSelected) {
            textLines.add(root)
        }

        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                findAllTextViews(root.getChildAt(i))
            }
        }
    }

    private fun changeAllColors(clicked: TextView) {
        colorIndex = (colorIndex + 1) % colors.size
        val newColor = colors[colorIndex]

        textLines.forEach { it.setTextColor(newColor) }

        val text = colorsText[colorIndex]
        binding.toolbar.menuTitle.text = resources.getString(text)
    }
}