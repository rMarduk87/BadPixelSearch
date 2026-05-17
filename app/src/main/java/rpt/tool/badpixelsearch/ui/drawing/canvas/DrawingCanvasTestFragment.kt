package rpt.tool.badpixelsearch.ui.drawing.canvas

import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentDrawingCanvasTestBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager


class DrawingCanvasTestFragment : BaseFragment<FragmentDrawingCanvasTestBinding>(
    FragmentDrawingCanvasTestBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle,
            getTitleFromShared())
    }

    private fun getTitleFromShared(): String {
        when(SharedPreferencesManager.drawingOption){
            0-> return getString(R.string.drawing_test_1)
            1-> return getString(R.string.drawing_test_2)
            2-> return getString(R.string.drawing_test_3)
            3-> return getString(R.string.drawing_test_4)
        }
        return ""
    }
}