package rpt.tool.badpixelsearch.ui.fonts.italic

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentItalicFontBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import kotlin.collections.forEach

class ItalicFontFragment :
    BaseFragment<FragmentItalicFontBinding>(FragmentItalicFontBinding::inflate) {

    private val colors = listOf(
        Color.GRAY,
        Color.WHITE,
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.MAGENTA,
        Color.CYAN,
        Color.YELLOW
    )

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

        findAllTextViews(view)

        val initial = colors[0]
        textLines.forEach { it.setTextColor(initial) }

        textLines.forEach { line ->

            if(SharedPreferencesManager.IsBoldItalic){
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
        binding.txtSelected.text = resources.getString(text)
    }
}