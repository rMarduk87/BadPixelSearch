package rpt.tool.badpixelsearch.ui.animation.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.ThreeDTestActivity
import rpt.tool.badpixelsearch.TwoDTestActivity
import rpt.tool.badpixelsearch.databinding.TestsMenuSixBinding
import rpt.tool.badpixelsearch.utils.log.e
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class AnimationMenuFragment: BaseFragment<TestsMenuSixBinding>
    (TestsMenuSixBinding::inflate) {

    companion object {
        private const val PULSE_DURATION = 2500L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menuTitle.text = requireContext().getString(R.string.animation_test_full)

        binding.iconAnimated.setImageResource(R.drawable.ic_animation_tests)

        binding.text1.text = requireContext().getString(R.string.two)
        binding.text2.text = requireContext().getString(R.string.threed)
        binding.text3.text = requireContext().getString(R.string.two_gravity)
        binding.text4.text = requireContext().getString(R.string.three_gravity)
        binding.text5.text = requireContext().getString(R.string.moving_bars)
        binding.text6.text = requireContext().getString(R.string.rotation)

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
                SharedPreferencesManager.animTest2D = true
                startActivity(
                    Intent(
                        requireContext(),
                        TwoDTestActivity::class.java
                    )
                )
            }
        }

        binding.option2.setOnClickListener{
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.animTest3D = true
                startActivity(Intent(requireContext(),
                    ThreeDTestActivity::class.java))
            }
        }

        binding.option3.setOnClickListener {
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.animTest2DGravity = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentTo2dGravityFragment()
                )
            }
        }

        binding.option4.setOnClickListener {
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.animTest3DGravity = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentTo3dGravityFragment())
            }
        }

        binding.option5.setOnClickListener {
            executeWithSound {
                binding.touch5.visibility = View.VISIBLE
                SharedPreferencesManager.animTestMovingBars = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentToMovingBarsFragment())
            }
        }

        binding.option6.setOnClickListener {
            executeWithSound {
                binding.touch6.visibility = View.VISIBLE
                SharedPreferencesManager.animTestRotation = true
                safeNavController?.safeNavigate(
                    AnimationMenuFragmentDirections
                        .actionAnimationMenuFragmentToRotationFragment())
            }
        }

        binding.btnBack.setOnClickListener {
            try {
                if(SharedPreferencesManager.sound){
                    val mediaPlayer = MediaPlayer.create(requireContext(),
                        R.raw.goodbye)
                    mediaPlayer?.setOnCompletionListener { it.release() }
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
                e(Throwable(e),"Sound")
            }
            safeNavController?.popBackStack()
        }

    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.animTest2D) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.animTest3D) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.animTest2DGravity) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.animTest3DGravity) {
            binding.touch4.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.animTestMovingBars) {
            binding.touch5.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.animTestRotation) {
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
