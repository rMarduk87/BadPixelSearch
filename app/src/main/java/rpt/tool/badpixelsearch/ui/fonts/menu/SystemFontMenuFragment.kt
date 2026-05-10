package rpt.tool.badpixelsearch.ui.fonts.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.TestsMenuSixBinding
import rpt.tool.badpixelsearch.utils.log.e
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate

class SystemFontMenuFragment: BaseFragment<TestsMenuSixBinding>
    (TestsMenuSixBinding::inflate) {

    private val PULSE_DURATION = 2500L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(R.string.system_fonts))
        binding.iconAnimated.setImageResource(R.drawable.ic_system_fonts)

        binding.text1.text = requireContext().getString(R.string.normal_fonts)
        binding.text2.text = requireContext().getString(R.string.italic_fonts)
        binding.text3.text = requireContext().getString(R.string.font_families)
        binding.text4.text = requireContext().getString(R.string.reading_test)

        // Hide unused options
        binding.option5.visibility = View.GONE
        binding.option6.visibility = View.GONE

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
                SharedPreferencesManager.fontTestNormal = true
                SharedPreferencesManager.IsBold = false
                safeNavController?.safeNavigate(
                    SystemFontMenuFragmentDirections.
                    actionSystemFontMenuFragmentToNormalFontFragment())
            }
        }

        binding.option2.setOnClickListener {
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.fontTestItalic = true
                SharedPreferencesManager.IsBoldItalic = false
                safeNavController?.safeNavigate(
                    SystemFontMenuFragmentDirections.
                    actionSystemFontMenuFragmentToItalicFontFragment()
                )
            }
        }

        binding.option3.setOnClickListener {
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.fontTestFamilies = true
                safeNavController?.safeNavigate(
                    SystemFontMenuFragmentDirections
                        .actionSystemFontMenuFragmentToFontFamiliesFragment())
            }
        }

        binding.option4.setOnClickListener {
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.fontTestReading = true
                safeNavController?.safeNavigate(
                    SystemFontMenuFragmentDirections.
                    actionSystemFontMenuFragmentToReadingTestFragment()
                )
            }
        }
    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.fontTestNormal) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fontTestItalic) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fontTestFamilies) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fontTestReading) {
            binding.touch4.visibility = View.VISIBLE
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
