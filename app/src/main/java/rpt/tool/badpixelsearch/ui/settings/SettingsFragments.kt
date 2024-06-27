package rpt.tool.badpixelsearch.ui.settings

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.RowItemSpeedBinding
import rpt.tool.badpixelsearch.databinding.SettingsFragmentsBinding
import rpt.tool.badpixelsearch.utils.AppUtils
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class SettingsFragments : BaseFragment<SettingsFragmentsBinding>(SettingsFragmentsBinding::inflate) {

    var mDropdown: PopupWindow? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftIconBlock.setOnClickListener{ finish() }
        binding.switchAutomaticMode.setChecked(SharedPreferencesManager.mode == 1)
        binding.switchFixMode.setChecked(SharedPreferencesManager.mode == 2)
        binding.switchFixLightMode.setChecked(SharedPreferencesManager.fullBrightness)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.navigationBarColor = requireContext().resources.getColor(R.color.black)
        }

        if(SharedPreferencesManager.mode == 1){
            binding.speedSuperBlock.visibility = View.VISIBLE
            binding.interval.visibility = View.VISIBLE
            binding.scrollView.visibility = View.VISIBLE
            binding.brightness.visibility = View.GONE
            binding.delayBlock.visibility = View.GONE
        }
        else{
            binding.speedSuperBlock.visibility = View.GONE
            binding.interval.visibility = View.GONE
            binding.scrollView.visibility = View.GONE
        }

        if(SharedPreferencesManager.mode==2){
            binding.speedSuperBlock.visibility = View.GONE
            binding.interval.visibility = View.GONE
            binding.scrollView.visibility = View.GONE
            binding.brightness.visibility = View.VISIBLE
            binding.delayBlock.visibility = View.VISIBLE
        }

        binding.switchAutomaticMode.setOnCheckedChangeListener{ _, isChecked ->
            SharedPreferencesManager.mode = if (isChecked) 1 else 0
            if(isChecked){
                binding.switchFixMode.setChecked(false)
                binding.speedSuperBlock.visibility = View.VISIBLE
                binding.interval.visibility = View.VISIBLE
                binding.scrollView.visibility = View.VISIBLE
                binding.brightness.visibility = View.GONE
                binding.delayBlock.visibility = View.GONE
            }
            else{
                binding.speedSuperBlock.visibility = View.GONE
                binding.interval.visibility = View.GONE
                binding.scrollView.visibility = View.GONE
            }
        }

        binding.switchFixMode.setOnCheckedChangeListener{ it, isChecked ->
            SharedPreferencesManager.mode = if (isChecked) 2 else 0
            if(isChecked){
                binding.switchAutomaticMode.setChecked(false)
                binding.speedSuperBlock.visibility = View.GONE
                binding.interval.visibility = View.GONE
                binding.scrollView.visibility = View.GONE
                binding.brightness.visibility = View.VISIBLE
                binding.delayBlock.visibility = View.VISIBLE
            }
            else{
                binding.brightness.visibility = View.GONE
                binding.delayBlock.visibility = View.GONE
            }
        }

        binding.switchFixLightMode.setOnCheckedChangeListener{ _, isChecked ->
            SharedPreferencesManager.fullBrightness = isChecked
        }

        binding.speedBlock.setOnClickListener{
            initiateSpeedPopupWindow(binding.switchAutomaticMode)
        }

        binding.txtSpeed.text = if(SharedPreferencesManager.velocity == 0)
            requireContext().getString(R.string.normal)
        else
            requireContext().getString(R.string.fast)

        binding.rdo1.text = "1 " + requireContext().getString(R.string.min)
        binding.rdo5.text = "5 " + requireContext().getString(R.string.min)
        binding.rdo15.text = "10 " + requireContext().getString(R.string.min)
        binding.rdo30.text = "30 " + requireContext().getString(R.string.min)
        binding.rdo60.text = "1 " + requireContext().getString(R.string.hour)

        binding.rdo1.setOnClickListener { saveInterval() }
        binding.rdo5.setOnClickListener { saveInterval() }
        binding.rdo15.setOnClickListener { saveInterval() }
        binding.rdo30.setOnClickListener { saveInterval() }
        binding.rdo60.setOnClickListener { saveInterval() }

        binding.rdo1.isChecked = SharedPreferencesManager.interval == AppUtils.one
        binding.rdo5.isChecked = SharedPreferencesManager.interval == AppUtils.five
        binding.rdo15.isChecked = SharedPreferencesManager.interval == AppUtils.fifthy
        binding.rdo30.isChecked = SharedPreferencesManager.interval == AppUtils.thirty
        binding.rdo60.isChecked = SharedPreferencesManager.interval == AppUtils.hour

        binding.delay.progress = SharedPreferencesManager.delay
        binding.delay.progressDrawable.setColorFilter(requireContext().getColor(R.color.white), PorterDuff.Mode.MULTIPLY)
    }

    private fun finish() {
        SharedPreferencesManager.delay = binding.delay.progress
        safeNavController?.safeNavigate(
            SettingsFragmentsDirections.actionSettingsFragmentToMenuFragment())
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

    private fun saveInterval() {
        var interval = if(binding.rdo1.isChecked){
            1
        }
        else if(binding.rdo5.isChecked){
            5
        }
        else if(binding.rdo15.isChecked){
            15
        }
        else if(binding.rdo30.isChecked){
            30
        }
        else{
            60
        }

        SharedPreferencesManager.interval = (interval*60000)
    }
}