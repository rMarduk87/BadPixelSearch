package rpt.tool.badpixelsearch.ui.animation.menu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.ThreeDTestActivity
import rpt.tool.badpixelsearch.TwoDTestActivity
import rpt.tool.badpixelsearch.databinding.FragmentAnimationMenuBinding
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class AnimationMenuFragment: BaseFragment<FragmentAnimationMenuBinding>
    (FragmentAnimationMenuBinding::inflate) {

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

        binding.openthreeDTest.setOnClickListener{
            startActivity(Intent(requireContext(),
                ThreeDTestActivity::class.java))
        }

        binding.opentwoDTest.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    TwoDTestActivity::class.java
                )
            )
        }

        binding.openMovingBarTest.setOnClickListener {
            safeNavController?.safeNavigate(
                AnimationMenuFragmentDirections
                    .actionAnimationMenuFragmentToMovingBarsFragment())
        }

        binding.openRotationTest.setOnClickListener {
            safeNavController?.safeNavigate(
                AnimationMenuFragmentDirections
                    .actionAnimationMenuFragmentToRotationFragment())
        }

        binding.openthreeDGravityTest.setOnClickListener {
            safeNavController?.safeNavigate(
                AnimationMenuFragmentDirections
                    .actionAnimationMenuFragmentTo3dGravityFragment()
            )
        }
    }
}