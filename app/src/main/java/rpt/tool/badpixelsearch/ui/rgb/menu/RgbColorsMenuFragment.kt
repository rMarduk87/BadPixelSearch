package rpt.tool.badpixelsearch.ui.rgb.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.TestsMenuSixBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate
import java.util.Locale.getDefault

class RgbColorsMenuFragment :
    BaseFragment<TestsMenuSixBinding>(TestsMenuSixBinding::inflate,false) {

    private val PULSE_DURATION = 2500L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle,
            getString(R.string.rgb_test).uppercase(getDefault())
        )
        binding.iconAnimated.setImageResource(R.drawable.splash)

        binding.text1.text = requireContext().getString(R.string.red_level)
        binding.text2.text = requireContext().getString(R.string.green_level)
        binding.text3.text = requireContext().getString(R.string.blue_level)
        binding.text4.text = requireContext().getString(R.string.gray_level)
        binding.text5.text = requireContext().getString(R.string.color_all)
        binding.text6.text = requireContext().getString(R.string.color_mixer)

        setupNumbers()

        ObjectAnimator.ofFloat(binding.iconAnimated, "alpha", 1f, 0f).apply {
            duration = PULSE_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        binding.option1.setOnClickListener{
            executeWithSound {
                binding.touch1.visibility = View.VISIBLE
                SharedPreferencesManager.rgbTestRed = true
                SharedPreferencesManager.rgbOption = 0
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    RgbColorsMenuFragmentDirections
                        .actionRgbColorsMenuFragmentToRgbColorsLevelFragment()
                )
            }
        }

        binding.option2.setOnClickListener{
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.rgbTestGreen = true
                SharedPreferencesManager.rgbOption = 1
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    RgbColorsMenuFragmentDirections
                        .actionRgbColorsMenuFragmentToRgbColorsLevelFragment()
                )
            }
        }

        binding.option3.setOnClickListener{
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.rgbTestBlue = true
                SharedPreferencesManager.rgbOption = 2
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    RgbColorsMenuFragmentDirections
                        .actionRgbColorsMenuFragmentToRgbColorsLevelFragment()
                )
            }
        }

        binding.option4.setOnClickListener{
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.rgbTestGray = true
                SharedPreferencesManager.rgbOption = 3
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    RgbColorsMenuFragmentDirections
                        .actionRgbColorsMenuFragmentToRgbColorsLevelFragment()
                )
            }
        }

        binding.option5.setOnClickListener {
            executeWithSound {
                binding.touch5.visibility = View.VISIBLE
                SharedPreferencesManager.rgbTestAll = true
                SharedPreferencesManager.rgbOption = 4
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    RgbColorsMenuFragmentDirections
                        .actionRgbColorsMenuFragmentToRgbColorsLevelFragment()
                )
            }
        }

        binding.option6.setOnClickListener {
            executeWithSound {
                binding.touch6.visibility = View.VISIBLE
                SharedPreferencesManager.rgbTestMixer = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(RgbColorsMenuFragmentDirections
                    .actionRgbColorsMenuFragmentToRgbColorMixerFragment())
            }
        }
    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.rgbTestRed) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.rgbTestGreen) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.rgbTestBlue) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.rgbTestGray) {
            binding.touch4.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.rgbTestAll) {
            binding.touch5.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.rgbTestMixer) {
            binding.touch6.visibility = View.VISIBLE
        }
    }

    private fun executeWithSound(action: () -> Unit) {
        if (SharedPreferencesManager.sound) {
            try {
                val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.click_sound)
                mediaPlayer?.setOnCompletionListener { it.release() }
                mediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        action()
    }
}
