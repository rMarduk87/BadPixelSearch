package rpt.tool.badpixelsearch.ui.drawing.stylus

import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentStylusTestBinding

class StylusLInesTestFragment :
    BaseFragment<FragmentStylusTestBinding>(FragmentStylusTestBinding::inflate) {

    private var filledCount = 0
    private val total = 6

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.fill_all)
        )

        for (i in 0 until binding.grid.childCount) {
            val item = binding.grid.getChildAt(i)
            val bigCircle = item.findViewById<View>(R.id.bigCircle)
            val smallCircle = item.findViewById<View>(R.id.smallCircle)

            smallCircle.setOnClickListener {

                if (bigCircle.tag == "filled") return@setOnClickListener

                bigCircle.setBackgroundResource(R.drawable.circle_filled)
                bigCircle.tag = "filled"

                filledCount++
                updateTitle()
            }
        }

        updateTitle()
    }

    private fun updateTitle() {
        binding.toolbar.menuTitle.text = if (filledCount == total) {
            getString(R.string.test_completed)
        } else {
            buildString {
                append(filledCount.toString())
                append(" ")
                append(getString(R.string.hole_filled))
            }
        }
    }
}