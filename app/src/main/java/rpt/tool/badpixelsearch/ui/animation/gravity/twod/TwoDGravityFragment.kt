package rpt.tool.badpixelsearch.ui.animation.gravity.twod

import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.Fragment2dGravityBinding

class TwoDGravityFragment :
    BaseFragment<Fragment2dGravityBinding>(Fragment2dGravityBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(rpt.tool.badpixelsearch.R.string._2d_gravity))
    }
}