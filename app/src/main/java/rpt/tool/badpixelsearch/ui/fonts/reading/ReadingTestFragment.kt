package rpt.tool.badpixelsearch.ui.fonts.reading

import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentReadingTestBinding

class ReadingTestFragment :
    BaseFragment<FragmentReadingTestBinding>(FragmentReadingTestBinding::inflate) {

    var index = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSize(binding,R.string.xsmall,12f)

        binding.fontChange.setOnClickListener {
            index++
            if(index == 5){
                index = 0
            }
            when(index)
            {
                0->setSize(binding,R.string.xsmall,12f)
                1->setSize(binding, R.string.small,14f)
                2->setSize(binding,R.string.medium,16f)
                3->setSize(binding,R.string.large,18f)
                4->setSize(binding,R.string.xlarge,20f)
            }
        }
    }
    private fun setSize(binding: FragmentReadingTestBinding, id: Int, size: Float) {
        binding.txtSelected.text = getString(id)
        binding.text1.textSize = size
        binding.text2.textSize = size
        binding.text3.textSize = size
    }
}