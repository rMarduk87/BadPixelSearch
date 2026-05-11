package rpt.tool.badpixelsearch.ui.touch.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.TestsMenuSixBinding
import rpt.tool.badpixelsearch.ui.animation.menu.AnimationMenuFragment
import rpt.tool.badpixelsearch.ui.animation.menu.AnimationMenuFragmentDirections
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class TouchTestMenuFragment :
    BaseFragment<TestsMenuSixBinding>(TestsMenuSixBinding::inflate) {

    companion object {
        const val PULSE_DURATION = 2500L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle,
            getString(R.string.drawing_test_full))

        binding.iconAnimated.setImageResource(R.drawable.ic_touch_tests)

        binding.text1.text = requireContext().getString(R.string.touch_test_1)
        binding.text2.text = requireContext().getString(R.string.touch_test_2)
        binding.text3.text = requireContext().getString(R.string.touch_test_3)
        binding.text4.text = requireContext().getString(R.string.touch_test_4)
        binding.text5.text = requireContext().getString(R.string.touch_test_5)
        binding.text6.text = requireContext().getString(R.string.touch_test_6)

        setupNumbers()

        ObjectAnimator.ofFloat(binding.iconAnimated, "alpha", 1f,
            0f).apply {
            duration = PULSE_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        binding.option1.setOnClickListener {
            executeWithSound {
                binding.touch1.visibility = View.VISIBLE
                SharedPreferencesManager.singleTouch = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentToTwoDTestFragment()
                )
            }
        }

        binding.option2.setOnClickListener{
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.singleTouchTwo = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentToThreeDTestFragment()
                )
            }
        }

        binding.option3.setOnClickListener {
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.multiTouch = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentTo2dGravityFragment()
                )
            }
        }

        binding.option4.setOnClickListener {
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.multiTouchTwo = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentTo3dGravityFragment())
            }
        }

        binding.option5.setOnClickListener {
            executeWithSound {
                binding.touch5.visibility = View.VISIBLE
                SharedPreferencesManager.zoom = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentToMovingBarsFragment())
            }
        }

        binding.option6.setOnClickListener {
            executeWithSound {
                binding.touch6.visibility = View.VISIBLE
                SharedPreferencesManager.responseTime = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentToRotationFragment())
            }
        }

    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.singleTouch) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.singleTouchTwo) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.multiTouch) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.multiTouchTwo) {
            binding.touch4.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.zoom) {
            binding.touch5.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.responseTime) {
            binding.touch6.visibility = View.VISIBLE
        }
    }

    private fun executeWithSound(action: () -> Unit) {
        if (SharedPreferencesManager.sound) {
            try {
                val mediaPlayer = MediaPlayer.create(requireContext(),
                    R.raw.click_sound)
                mediaPlayer?.setOnCompletionListener {
                    it.release()
                }
                mediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        action()
    }
}