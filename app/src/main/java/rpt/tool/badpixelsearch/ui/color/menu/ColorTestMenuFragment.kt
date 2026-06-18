package rpt.tool.badpixelsearch.ui.color.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.TestsMenuSixBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate

class ColorTestMenuFragment: BaseFragment<TestsMenuSixBinding>
    (TestsMenuSixBinding::inflate,false) {

    companion object {
        private const val PULSE_DURATION = 2500L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(R.string.color_tests))

        binding.iconAnimated.setImageResource(R.drawable.ic_color_tests)

        binding.text1.text = requireContext().getString(R.string.purity)
        binding.text2.text = requireContext().getString(R.string.gradient)
        binding.text3.text = requireContext().getString(R.string.scales)
        binding.text4.text = requireContext().getString(R.string.shades)
        binding.text5.text = requireContext().getString(R.string.gamma_test)
        binding.text6.text = requireContext().getString(R.string.line_test)

        setupNumbers()

        ObjectAnimator.ofFloat(binding.iconAnimated, "alpha", 1f,
            0f).apply {
            duration = PULSE_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        binding.option1.setOnClickListener{
            executeWithSound {
                binding.touch1.visibility = View.VISIBLE
                SharedPreferencesManager.colorTestPurity = true
                SharedPreferencesManager.typeMode = 0
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    ColorTestMenuFragmentDirections
                        .actionColorTestsMenuFragmentToBadPixelSearchFragment()
                )
            }
        }

        binding.option2.setOnClickListener {
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.colorTestGradient = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    ColorTestMenuFragmentDirections
                        .actionColorTestsMenuFragmentToGradientTestFragment()
                )
            }
        }

        binding.option3.setOnClickListener {
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.colorTestScales = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(ColorTestMenuFragmentDirections
                    .actionColorTestsMenuFragmentToColorScalesFragment())
            }
        }

        binding.option4.setOnClickListener {
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.colorTestShades = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    ColorTestMenuFragmentDirections
                        .actionColorTestsMenuFragmentToColorShadeFragment()
                )
            }
        }

        binding.option5.setOnClickListener {
            executeWithSound {
                binding.touch5.visibility = View.VISIBLE
                SharedPreferencesManager.colorTestGamma = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    ColorTestMenuFragmentDirections
                        .actionColorTestsMenuFragmentToGammaColorFragment())
            }
        }

        binding.option6.setOnClickListener {
            executeWithSound {
                binding.touch6.visibility = View.VISIBLE
                SharedPreferencesManager.colorTestLine = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    ColorTestMenuFragmentDirections
                        .actionColorTestsMenuFragmentToColorLineTestFragment())
            }
        }

    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.colorTestPurity) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.colorTestGradient) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.colorTestScales) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.colorTestShades) {
            binding.touch4.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.colorTestGamma) {
            binding.touch5.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.colorTestLine) {
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