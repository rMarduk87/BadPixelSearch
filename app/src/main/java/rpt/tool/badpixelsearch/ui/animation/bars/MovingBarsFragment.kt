package rpt.tool.badpixelsearch.ui.animation.bars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentAnimationBarsBinding
import rpt.tool.badpixelsearch.utils.view.bars.MovingBarsView

class MovingBarsFragment : BaseFragment<FragmentAnimationBarsBinding>
    (FragmentAnimationBarsBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_animation_bars, container, false)
        val txtFps = root.findViewById<TextView>(R.id.txtMovingBars)


        val rootLayout = root.findViewById<RelativeLayout>(R.id.root)

        val barsView = MovingBarsView(requireContext()).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )

            fpsCallback = { fps ->
                activity?.runOnUiThread {
                    txtFps.text = buildString {
                        append(requireContext().getString(R.string.display_fps))
                        append(" ")
                        append(fps)
                    }
                }
            }
        }

        txtFps.post {
            barsView.setTopMargin(txtFps.bottom.toFloat())
        }

        rootLayout.addView(barsView, 0)

        return root
    }
}