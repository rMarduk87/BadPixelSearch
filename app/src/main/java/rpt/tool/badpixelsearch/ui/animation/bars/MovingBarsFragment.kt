package rpt.tool.badpixelsearch.ui.animation.bars

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentAnimationBarsBinding
import rpt.tool.badpixelsearch.utils.view.bars.MovingBarsView

class MovingBarsFragment : BaseFragment<FragmentAnimationBarsBinding>
    (FragmentAnimationBarsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.moving_bars)
        )

        val barsView = MovingBarsView(requireContext()).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )

            fpsCallback = { fps ->
                activity?.runOnUiThread {
                    binding.txtMovingBars.text = buildString {
                        append(requireContext().getString(R.string.display_fps))
                        append(" ")
                        append(fps)
                    }
                }
            }
        }

        binding.txtMovingBars.post {
            barsView.setTopMargin(binding.txtMovingBars.bottom.toFloat())
        }

        binding.root.addView(barsView, 0)
    }
}