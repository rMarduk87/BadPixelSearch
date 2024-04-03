package rpt.tool.badpixelsearch.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.RowItemSpeedBinding
import rpt.tool.badpixelsearch.databinding.SettingsFragmentsBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class SettingsFragments : BaseFragment<SettingsFragmentsBinding>(SettingsFragmentsBinding::inflate) {

    var mDropdown: PopupWindow? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftIconBlock.setOnClickListener{ finish() }
        binding.switchMode.setChecked(SharedPreferencesManager.mode == 1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.navigationBarColor = requireContext().resources.getColor(R.color.black)
        }

        if(SharedPreferencesManager.mode == 1){
            binding.speedSuperBlock.visibility = View.VISIBLE
        }
        else{
            binding.speedSuperBlock.visibility = View.GONE
        }

        binding.switchMode.setOnCheckedChangeListener{ _, isChecked ->
            SharedPreferencesManager.mode = if (isChecked) 1 else 0
            if(isChecked){
                binding.speedSuperBlock.visibility = View.VISIBLE
            }
            else{
                binding.speedSuperBlock.visibility = View.GONE
            }
        }

        binding.speedBlock.setOnClickListener{
            initiateSpeedPopupWindow(binding.switchMode)
        }

        binding.txtSpeed.text = if(SharedPreferencesManager.velocity == 0)
            requireContext().getString(R.string.normal)
        else
            requireContext().getString(R.string.fast)
    }

    private fun finish() {
        safeNavController?.safeNavigate(
            SettingsFragmentsDirections.actionSettingsFragmentToBadPixelSearchFragment())
    }

    private fun initiateSpeedPopupWindow(v: View): PopupWindow {
        try {
            val bindingRow: RowItemSpeedBinding = RowItemSpeedBinding.inflate(layoutInflater)

            bindingRow.lblNormal.text = requireContext().getString(R.string.normal)
            bindingRow.lblFast.text = requireContext().getString(R.string.fast)

            bindingRow.lblNormal.setOnClickListener {
                SharedPreferencesManager.velocity = 0
                mDropdown!!.dismiss()
                binding.txtSpeed.text = requireContext().getString(R.string.normal)
            }
            bindingRow.lblFast.setOnClickListener {
                SharedPreferencesManager.velocity = 1
                mDropdown!!.dismiss()
                binding.txtSpeed.text = requireContext().getString(R.string.fast)
            }
            bindingRow.root.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            mDropdown = PopupWindow(
                bindingRow.root, FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, true
            )

            mDropdown!!.showAsDropDown(v, 5, 5)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mDropdown!!
    }
}