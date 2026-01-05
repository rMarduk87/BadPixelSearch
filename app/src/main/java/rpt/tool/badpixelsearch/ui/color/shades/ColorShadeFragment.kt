package rpt.tool.badpixelsearch.ui.color.shades

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentColorShadeBinding

class ColorShadeFragment :
    BaseFragment<FragmentColorShadeBinding>(FragmentColorShadeBinding::inflate) {

    private val colorList = listOf(
        Color.GRAY,
        Color.WHITE,
        Color.BLACK,
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.MAGENTA,
        Color.CYAN,
        Color.YELLOW
    )

    private var colorNames = listOf<String>()

    private var colorIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorNames = listOf(
            requireContext().resources.getString(R.string.gray),
            requireContext().resources.getString(R.string.white),
            requireContext().resources.getString(R.string.black),
            requireContext().resources.getString(R.string.red),
            requireContext().resources.getString(R.string.green),
            requireContext().resources.getString(R.string.blue),
            requireContext().resources.getString(R.string.magenta),
            requireContext().resources.getString(R.string.cyan),
            requireContext().resources.getString(R.string.yellow))

        setupBoxes()
        applyColor()

        binding.mainContainer.setOnClickListener {
            colorIndex = (colorIndex + 1) % colorList.size
            applyColor()
        }
    }

    private fun setupBoxes() {
        for (i in 1..16) {
            val tv = TextView(requireContext()).apply {
                text = i.toString()
                textSize = 22f
                setPadding(0, 35, 0, 35)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 6, 0, 6)
                }
            }
            binding.boxContainer.addView(tv)
        }
    }

    private fun applyColor() {
        val currentColor = colorList[colorIndex]
        val name = colorNames[colorIndex]

        binding.mainContainer.setBackgroundColor(currentColor)

        for (i in 0 until binding.boxContainer.childCount) {
            val tv = binding.boxContainer.getChildAt(i) as TextView

            val shaded: Int

            if (name == "Gray") {

                val baseGray = 0x8E

                val alpha = (18 + i * 3).coerceIn(0, 120)

                shaded = Color.argb(alpha, baseGray, baseGray, baseGray)
            } else {

                val alpha = (30 + (i * 6)).coerceIn(0, 255)
                val r = Color.red(currentColor)
                val g = Color.green(currentColor)
                val b = Color.blue(currentColor)
                shaded = Color.argb(alpha, r, g, b)
            }

            tv.setBackgroundColor(shaded)

            tv.setTextColor(
                if (isColorDark(Color.argb(255,
                        Color.red(currentColor),
                        Color.green(currentColor),
                        Color.blue(currentColor)
                    ))) Color.WHITE else Color.BLACK
            )
        }

        binding.txtTitle.text = buildString {
            append(getString(R.string._16_shades_of))
            append(" ")
            append(name)
        }
        binding.txtTitle.setTextColor(
            if (isColorDark(currentColor)) Color.WHITE else Color.BLACK
        )
    }



    private fun shadeColor(baseColor: Int, factor: Float): Int {
        val r = ((Color.red(baseColor) * factor).toInt()).coerceIn(0, 255)
        val g = ((Color.green(baseColor) * factor).toInt()).coerceIn(0, 255)
        val b = ((Color.blue(baseColor) * factor).toInt()).coerceIn(0, 255)
        return Color.rgb(r, g, b)
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color)
                + 0.587 * Color.green(color)
                + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}