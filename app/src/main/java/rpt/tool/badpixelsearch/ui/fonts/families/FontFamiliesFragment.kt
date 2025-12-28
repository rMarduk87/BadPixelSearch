package rpt.tool.badpixelsearch.ui.fonts.families

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentFontFamiliesBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class FontFamiliesFragment :
    BaseFragment<FragmentFontFamiliesBinding>(FragmentFontFamiliesBinding::inflate) {

    private val colors = listOf(
        Color.GRAY, Color.WHITE, Color.RED, Color.GREEN,
        Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW
    )

    private val colorsText = listOf(
        R.string.gray_text, R.string.white_text, R.string.red_text,
        R.string.green_text, R.string.blue_text, R.string.magenta_text,
        R.string.cyan_text, R.string.yellow_text
    )

    private var colorIndex = 0
    private var fontIndex = 0
    private var currentColor: Int = Color.GRAY

    private val titleIds = setOf(
        R.id.title_xsmall, R.id.title_small,
        R.id.title_medium, R.id.title_large
    )

    private val textLines = mutableListOf<TextView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findAllTextViews()

        applyColor(colors[0])
        applyBoldItalic()

        textLines.forEach { tv ->
            tv.setOnClickListener { changeAllColors() }
        }
        
        binding.fontChange.setOnClickListener { changeFonts() }
    }

    private fun applyBoldItalic() {
        val style =
            if (SharedPreferencesManager.IsBoldItalic) Typeface.BOLD else Typeface.NORMAL

        textLines.forEach {
            it.setTypeface(it.typeface, style)
        }
    }

    private fun findAllTextViews() {
        textLines.clear()
        collectTextViews(binding.root)
    }

    private fun collectTextViews(view: View?) {
        if (view == null) return

        if (view is TextView) {
            val id = view.id

            if (id != R.id.txtSelected && id !in titleIds) {
                textLines.add(view)
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                collectTextViews(view.getChildAt(i))
            }
        }
    }

    private fun changeAllColors() {
        colorIndex = (colorIndex + 1) % colors.size
        val newColor = colors[colorIndex]

        applyColor(newColor)

        binding.txtSelected.text = getString(colorsText[colorIndex])
    }

    private fun applyColor(color: Int) {
        currentColor = color
        textLines.forEach { it.setTextColor(color) }
    }

    private fun changeFonts() {
        fontIndex = (fontIndex + 1) % 3

        when (fontIndex) {
            0 -> applyFontSet(FontSet.CALIBRI)
            1 -> applyFontSet(FontSet.BALOO)
            2 -> applyFontSet(FontSet.LIGHT)
        }

        binding.root.post {
            applyColor(currentColor)
            applyBoldItalic()
        }
    }

    private enum class FontSet { CALIBRI, BALOO, LIGHT }

    private fun applyFontSet(set: FontSet) {

        val xs: Typeface
        val sm: Typeface
        val md: Typeface
        val lg: Typeface

        when (set) {

            FontSet.CALIBRI -> {
                xs = ResourcesCompat.getFont(requireContext(),
                    R.font.calibri_regular)!!
                sm = Typeface.create("sans-serif", Typeface.NORMAL)
                md = ResourcesCompat.getFont(requireContext(), R.font.kanit)!!
                lg = ResourcesCompat.getFont(requireContext(),
                    R.font.great_vibes_regular)!!

                binding.titleXsmall.setText(R.string.calibri)
                binding.titleSmall.setText(R.string.open_sans)
                binding.titleMedium.setText(R.string.kanit)
                binding.titleLarge.setText(R.string.cursive)
            }

            FontSet.BALOO -> {
                xs = ResourcesCompat.getFont(requireContext(), R.font.baloo)!!
                sm = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
                md = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
                lg = ResourcesCompat.getFont(requireContext(),
                    R.font.muhaqu_regular_personaluse)!!

                binding.titleXsmall.setText(R.string.baloo)
                binding.titleSmall.setText(R.string.monospace)
                binding.titleMedium.setText(R.string.bold_monospace)
                binding.titleLarge.setText(R.string.muhaqu)
            }

            FontSet.LIGHT -> {
                xs = ResourcesCompat.getFont(requireContext(), R.font.calibri_light)!!
                sm = Typeface.create("sans-serif-light", Typeface.NORMAL)
                md = Typeface.create("sans-serif-smallcaps", Typeface.NORMAL)
                lg = Typeface.create("sans-serif-thin", Typeface.NORMAL)

                binding.titleXsmall.setText(R.string.calibir_light)
                binding.titleSmall.setText(R.string.sans)
                binding.titleMedium.setText(R.string.sans_small)
                binding.titleLarge.setText(R.string.sans_thin)
            }
        }

        applyLines(xs, binding.xsLine1, binding.xsLine2, binding.xsLine3)
        applyLines(sm, binding.smLine1, binding.smLine2, binding.smLine3)
        applyLines(md, binding.mdLine1, binding.mdLine2, binding.mdLine3)
        applyLines(lg, binding.lgLine1, binding.lgLine2, binding.lgLine3)
    }

    private fun applyLines(typeface: Typeface, vararg lines: TextView) {
        lines.forEach { it.typeface = typeface }
    }
}