package rpt.tool.badpixelsearch.ui.touch.multi

import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentMultiTouchTwoTestBinding

class MultiTouchTwoTestFragment :
    BaseFragment<FragmentMultiTouchTwoTestBinding>(FragmentMultiTouchTwoTestBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.touch_the_screen)
        )

        binding.touchView.onFingerChange = { count ->
            binding.toolbar.menuTitle.text = buildString {
                append(getString(R.string.fingers))
                append(count)
            }
        }
    }
}