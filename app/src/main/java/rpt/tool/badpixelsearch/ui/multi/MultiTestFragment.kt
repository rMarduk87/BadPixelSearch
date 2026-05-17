package rpt.tool.badpixelsearch.ui.multi

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentMultiTestBinding

class MultiTestFragment: BaseFragment<FragmentMultiTestBinding>(FragmentMultiTestBinding::inflate) {

    private var currentState = 0

    private val photos = intArrayOf(
        R.drawable.foto1,
        R.drawable.foto2,
        R.drawable.foto3,
        R.drawable.foto4,
        R.drawable.foto5,
        R.drawable.foto6
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()

        binding.rootContainer.setOnClickListener {
            val totalStates = 3 + photos.size

            if (currentState == totalStates - 1) {
                parentFragmentManager.popBackStack()

            } else {
                currentState++
                updateUI()
            }
        }
    }

    private fun updateUI() {
        when (currentState) {
            0 -> {
                binding.photoView.visibility = View.GONE
                binding.blocksContainer.visibility = View.VISIBLE
                binding.blocksContainer.background = null
                val grayColors = intArrayOf(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    ContextCompat.getColor(requireContext(), R.color.light_gray_E0),
                    ContextCompat.getColor(requireContext(), R.color.light_gray_C0),
                    ContextCompat.getColor(requireContext(), R.color.light_gray_A0),
                    ContextCompat.getColor(requireContext(), R.color.gray_80),
                    ContextCompat.getColor(requireContext(), R.color.gray_60),
                    ContextCompat.getColor(requireContext(), R.color.gray_40),
                    ContextCompat.getColor(requireContext(), R.color.gray_20)
                )
                drawBlocks(grayColors)
            }
            1 -> {
                binding.photoView.visibility = View.GONE
                binding.blocksContainer.visibility = View.VISIBLE
                binding.blocksContainer.background = null
                val rainbowBlocks = intArrayOf(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    ContextCompat.getColor(requireContext(), R.color.yellow),
                    ContextCompat.getColor(requireContext(), R.color.cyan),
                    ContextCompat.getColor(requireContext(), R.color.green),
                    ContextCompat.getColor(requireContext(), R.color.magenta),
                    ContextCompat.getColor(requireContext(), R.color.red),
                    ContextCompat.getColor(requireContext(), R.color.blue)
                )
                drawBlocks(rainbowBlocks)
            }
            2 -> {
                binding.photoView.visibility = View.GONE
                binding.blocksContainer.visibility = View.VISIBLE
                binding.blocksContainer.removeAllViews()

                val gradientColors = intArrayOf(
                    ContextCompat.getColor(requireContext(), R.color.red),
                    ContextCompat.getColor(requireContext(), R.color.orange_material),
                    ContextCompat.getColor(requireContext(), R.color.yellow),
                    ContextCompat.getColor(requireContext(), R.color.green),
                    ContextCompat.getColor(requireContext(), R.color.cyan),
                    ContextCompat.getColor(requireContext(), R.color.blue),
                    ContextCompat.getColor(requireContext(), R.color.magenta)
                )
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    gradientColors
                )
                binding.blocksContainer.background = gradientDrawable
            }
            else -> {
                binding.photoView.visibility = View.VISIBLE
                binding.blocksContainer.visibility = View.GONE

                val photoIndex = currentState - 3
                binding.photoView.setImageResource(photos[photoIndex])
            }
        }
    }

    private fun drawBlocks(colors: IntArray) {
        binding.blocksContainer.removeAllViews()
        for (color in colors) {
            val view = View(requireContext())
            view.setBackgroundColor(color)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f
            )
            view.layoutParams = params
            binding.blocksContainer.addView(view)
        }
    }
}

