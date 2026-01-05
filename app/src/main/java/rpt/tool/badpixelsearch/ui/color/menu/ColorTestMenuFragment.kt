package rpt.tool.badpixelsearch.ui.color.menu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BadPixelSearchActivity
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.GradientTestActivity
import rpt.tool.badpixelsearch.databinding.FragmentMenuColorTestBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class ColorTestMenuFragment: BaseFragment<FragmentMenuColorTestBinding>
    (FragmentMenuColorTestBinding::inflate) {

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

        binding.openPurityTest.setOnClickListener{
            SharedPreferencesManager.typeMode = 0
            startActivity(Intent(requireContext(),
                BadPixelSearchActivity::class.java))
        }

        binding.openGradientTest.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    GradientTestActivity::class.java
                )
            )
        }

        binding.openScalesTest.setOnClickListener {
            safeNavController?.safeNavigate(ColorTestMenuFragmentDirections
                .actionColorTestsMenuFragmentToColorScalesFragment())
        }

        binding.openShadesTest.setOnClickListener {
            safeNavController?.safeNavigate(
                ColorTestMenuFragmentDirections
                    .actionColorTestsMenuFragmentToColorShadeFragment()
            )
        }

        binding.openGammaTest.setOnClickListener {
            safeNavController?.safeNavigate(
                ColorTestMenuFragmentDirections
                    .actionColorTestsMenuFragmentToGammaColorFragment())
        }

        binding.openLineTest.setOnClickListener {
            safeNavController?.safeNavigate(
                ColorTestMenuFragmentDirections
                    .actionColorTestsMenuFragmentToColorLineTestFragment())
        }

    }
}