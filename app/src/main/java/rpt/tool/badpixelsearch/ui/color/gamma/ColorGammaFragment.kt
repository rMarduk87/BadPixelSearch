package rpt.tool.badpixelsearch.ui.color.gamma

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentGammaColorBinding
import kotlin.math.pow

class ColorGammaFragment :
    BaseFragment<FragmentGammaColorBinding>(FragmentGammaColorBinding::inflate) {

    private lateinit var baseColors: List<Pair<String, Int>>

    private var currentIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseColors = listOf(
            Pair(getString(R.string.gray), Color.GRAY),
            Pair(getString(R.string.red), Color.RED),
            Pair(getString(R.string.green), Color.GREEN),
            Pair(getString(R.string.blue), Color.BLUE),
            Pair(getString(R.string.magenta), Color.MAGENTA),
            Pair(getString(R.string.cyan), Color.CYAN),
            Pair(getString(R.string.yellow), Color.YELLOW)
        )

        // 3. Ensure the initial text is set (since the list was just created)
        binding.txtTitle.text = baseColors[currentIndex].first

        createGammaBars(baseColors[currentIndex].second)

        binding.mainContainer.setOnClickListener {
            currentIndex = (currentIndex + 1) % baseColors.size
            binding.txtTitle.text = baseColors[currentIndex].first
            createGammaBars(baseColors[currentIndex].second)
        }
    }

    private fun createGammaBars(baseColor: Int) {
        val container = binding.gammaContainer
        container.removeAllViews()

        val gammaValues = listOf(
            1.2f,1.3f,1.4f,1.5f,1.6f,1.7f,1.8f,1.9f,
            2.0f,2.1f,2.2f,2.3f,2.4f,2.5f,2.6f,2.7f,2.8f,2.9f,3.0f
        )

        gammaValues.forEach { gamma ->
            val bar = TextView(requireContext())
            bar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )

            val r = ((Color.red(baseColor) / 255f).pow(1f / gamma) * 255).toInt()
            val g = ((Color.green(baseColor) / 255f).pow(1f / gamma) * 255).toInt()
            val b = ((Color.blue(baseColor) / 255f).pow(1f / gamma) * 255).toInt()

            bar.setBackgroundColor(Color.rgb(r, g, b))

            bar.text = gamma.toString()
            bar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            bar.textSize = 17f
            bar.setTextColor(Color.BLACK)

            container.addView(bar)
        }
    }
}