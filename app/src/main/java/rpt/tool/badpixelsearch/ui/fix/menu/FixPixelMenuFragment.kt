package rpt.tool.badpixelsearch.ui.fix.menu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BadPixelSearchActivity
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.FixPixelActivity
import rpt.tool.badpixelsearch.NoiseSearchActivity
import rpt.tool.badpixelsearch.PixelTestActivity
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentFixPixelsMenuBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class FixPixelMenuFragment: BaseFragment<FragmentFixPixelsMenuBinding>
    (FragmentFixPixelsMenuBinding::inflate) {

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

        binding.openFixTest.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    FixPixelActivity::class.java
                )
            )
        }

        binding.openBWTest.setOnClickListener {
            SharedPreferencesManager.typeMode = 1
            startActivity(Intent(requireContext(),
                BadPixelSearchActivity::class.java))
        }

        binding.openNoiseTest.setOnClickListener {
            SharedPreferencesManager.typeMode = 2
            SharedPreferencesManager.typeNoiseColored = false
            startActivity(Intent(requireContext(),
                NoiseSearchActivity::class.java))
        }

        binding.openChangeSnowFlakes.setOnClickListener {
            SharedPreferencesManager.typeMode = 2
            SharedPreferencesManager.typeNoiseColored = true
            startActivity(Intent(requireContext(),
                NoiseSearchActivity::class.java))
        }

        binding.openHorizontalLineTest.setOnClickListener {
            SharedPreferencesManager.typeMode = 3
            SharedPreferencesManager.isVertical = false
            startActivity(Intent(requireContext(),
                PixelTestActivity::class.java))

        }

        binding.openVerticalLineTest.setOnClickListener {
            SharedPreferencesManager.typeMode = 4
            SharedPreferencesManager.isVertical = true
            startActivity(Intent(requireContext(),
                PixelTestActivity::class.java))
        }

        binding.openHorizontalRectangleTest.setOnClickListener {
            SharedPreferencesManager.typeMode = 5
            SharedPreferencesManager.isVertical = false
            startActivity(Intent(requireContext(),
                PixelTestActivity::class.java))
        }

        binding.openVerticalRectangleTest.setOnClickListener {
            SharedPreferencesManager.typeMode = 6
            SharedPreferencesManager.isVertical = true
            startActivity(Intent(requireContext(),
                PixelTestActivity::class.java))
        }
    }
}


/*binding.include.openAnimationTestBtn.setOnClickListener{
           when(SharedPreferencesManager.typeMode){
               0,1->startActivity(Intent(requireContext(),
                   BadPixelSearchActivity::class.java))
               2->startActivity(Intent(requireContext(),
                   NoiseSearchActivity::class.java))
               3,4,5,6->startActivity(Intent(requireContext(),
                   PixelTestActivity::class.java))
               7->startActivity(Intent(requireContext(),
                   GradientTestActivity::class.java))
           }
       }*/