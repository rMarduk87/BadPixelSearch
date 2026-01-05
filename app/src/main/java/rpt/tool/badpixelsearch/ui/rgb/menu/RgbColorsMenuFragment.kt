package rpt.tool.badpixelsearch.ui.rgb.menu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.RgbColorsLevelActivity
import rpt.tool.badpixelsearch.databinding.FragmentRgbMenuBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class RgbColorsMenuFragment :
    BaseFragment<FragmentRgbMenuBinding>(FragmentRgbMenuBinding::inflate) {

    private val durationValue = 6000L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        val width = binding.logoAnimated.measuredWidth.toFloat()

        val animator1 = ObjectAnimator
            .ofFloat(binding.logoAnimated,
                "translationX", 0f, -(width - point.x)).apply {
                duration = durationValue
                repeatCount = 1
                repeatMode = ValueAnimator.REVERSE
            }

        val animator2 = ObjectAnimator
            .ofFloat(binding.logoAnimated,"translationX",
                0f, +(width - point.x)).apply {
                duration = durationValue
                repeatCount = 1
                repeatMode = ValueAnimator.REVERSE
            }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2)
        animatorSet.start()

        binding.openRedTest.setOnClickListener{
            SharedPreferencesManager.rgbOption = 0
            startActivity(
                Intent(
                    requireContext(),
                    RgbColorsLevelActivity::class.java
                )
            )
        }

        binding.openGreenTest.setOnClickListener{
            SharedPreferencesManager.rgbOption = 1
            startActivity(Intent(requireContext(),
                RgbColorsLevelActivity::class.java))
        }

        binding.openBlueTest.setOnClickListener{
            SharedPreferencesManager.rgbOption = 2
            startActivity(Intent(requireContext(),
                RgbColorsLevelActivity::class.java))
        }

        binding.openGrayTest.setOnClickListener{
            SharedPreferencesManager.rgbOption = 3
            startActivity(Intent(requireContext(),
                RgbColorsLevelActivity::class.java))
        }

        binding.openAllColorsTest.setOnClickListener {
            SharedPreferencesManager.rgbOption = 4
            startActivity(Intent(requireContext(),
                RgbColorsLevelActivity::class.java))
        }

        binding.openColorMixerTest.setOnClickListener {
            safeNavController?.safeNavigate(RgbColorsMenuFragmentDirections
                .actionRgbColorsMenuFragmentToRgbColorMixerFragment())
        }


    }
}