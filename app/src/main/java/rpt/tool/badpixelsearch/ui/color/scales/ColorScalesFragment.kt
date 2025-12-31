package rpt.tool.badpixelsearch.ui.color.scales

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentColorScalesBinding

class ColorScalesFragment:
    BaseFragment<FragmentColorScalesBinding>(FragmentColorScalesBinding::inflate) {

    private val percentages = listOf(
        100, 93, 87, 81, 75, 68, 62, 56,
        50, 43, 37, 31, 25, 18, 12, 6
    )

    private val colorModes = listOf(
        "White", "Red", "Green", "Blue", "Magenta", "Cyan", "Yellow"
    )

    private var currentColorIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateLevels()

        binding.mainLayout.setOnClickListener {
            currentColorIndex = (currentColorIndex + 1) % colorModes.size
            updateColors()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.txtTitle) { v, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.setPadding(0, topInset, 0, 0)
            insets
        }

    }

    private fun generateLevels() {
        binding.levelsContainer.removeAllViews()

        for (p in percentages) {
            val bar = TextView(requireContext())
            bar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            bar.text = buildString {
                append(p)
                append("%")
            }
            bar.textSize = 18f
            bar.gravity = android.view.Gravity.CENTER
            bar.setTextColor(Color.WHITE)

            binding.levelsContainer.addView(bar)
        }

        updateColors()
    }

    private fun updateColors() {
        val mode = colorModes[currentColorIndex]

        val (r, g, b) = when (mode) {
            "White" -> Triple(255, 255, 255)
            "Red" -> Triple(255, 0, 0)
            "Green" -> Triple(0, 255, 0)
            "Blue" -> Triple(0, 0, 255)
            "Magenta" -> Triple(255, 0, 255)
            "Cyan" -> Triple(0, 255, 255)
            "Yellow" -> Triple(255, 255, 0)
            else -> Triple(255, 255, 255)
        }

        binding.txtTitle.text = buildString {
            append(getString(R.string._16_levels_of))
            append(" ")
            append(mode)
        }

        for (i in 0 until binding.levelsContainer.childCount) {
            val bar = binding.levelsContainer.getChildAt(i) as TextView
            val alpha = percentages[i] / 100f

            val color = Color.argb((alpha * 255).toInt(), r, g, b)
            bar.setBackgroundColor(color)

            val luminance = (0.299 * r + 0.587 * g + 0.114 * b) * alpha

            bar.setTextColor(
                if (luminance > 130) Color.BLACK else Color.WHITE
            )
        }
    }

}