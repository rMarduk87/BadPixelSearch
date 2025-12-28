package rpt.tool.badpixelsearch.ui.fonts.menu

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
import rpt.tool.badpixelsearch.databinding.FragmentSystemFontMenuBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class SystemFontMenuFragment: BaseFragment<FragmentSystemFontMenuBinding>
    (FragmentSystemFontMenuBinding::inflate) {

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

        binding.openNormalFonts.setOnClickListener{
            SharedPreferencesManager.IsBold = false
            safeNavController?.safeNavigate(
                SystemFontMenuFragmentDirections.
                actionSystemFontMenuFragmentToNormalFontFragment())
        }

        binding.openItalicFonts.setOnClickListener {
            SharedPreferencesManager.IsBoldItalic = false
            safeNavController?.safeNavigate(
                SystemFontMenuFragmentDirections.
                actionSystemFontMenuFragmentToItalicFontFragment()
            )
        }

        binding.openBoldFonts.setOnClickListener {
            SharedPreferencesManager.IsBold = true
            safeNavController?.safeNavigate(
                SystemFontMenuFragmentDirections.
                actionSystemFontMenuFragmentToNormalFontFragment())
        }

        binding.openBoldItalicFonts.setOnClickListener {
            SharedPreferencesManager.IsBoldItalic = true
            safeNavController?.safeNavigate(
                SystemFontMenuFragmentDirections.
                actionSystemFontMenuFragmentToItalicFontFragment()
            )
        }

        binding.openFontFamilies.setOnClickListener {
            safeNavController?.safeNavigate(
                SystemFontMenuFragmentDirections
                    .actionSystemFontMenuFragmentToFontFamiliesFragment())
        }

        binding.openReadingTest.setOnClickListener {
            safeNavController?.safeNavigate(
                SystemFontMenuFragmentDirections.
                actionSystemFontMenuFragmentToReadingTestFragment()
            )
        }
    }
}