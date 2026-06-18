package rpt.tool.badpixelsearch.ui.drawing.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.TestsMenuSixBinding
import rpt.tool.badpixelsearch.ui.animation.menu.AnimationMenuFragmentDirections
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate

class DrawingTestMenuFragment : BaseFragment<TestsMenuSixBinding>(TestsMenuSixBinding::inflate,false) {

    companion object {
        const val PULSE_DURATION = 2500L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle,
            getString(R.string.drawing_test_full))

        binding.iconAnimated.setImageResource(R.drawable.ic_drawing_tests)

        binding.text1.text = requireContext().getString(R.string.drawing_test_1)
        binding.text2.text = requireContext().getString(R.string.drawing_test_2)
        binding.text3.text = requireContext().getString(R.string.drawing_test_3)
        binding.text4.text = requireContext().getString(R.string.drawing_test_4)
        binding.text5.text = requireContext().getString(R.string.drawing_test_5)
        binding.text6.text = requireContext().getString(R.string.drawing_test_6)

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
                SharedPreferencesManager.normalLines = true
                SharedPreferencesManager.drawingOption = 0
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    DrawingTestMenuFragmentDirections
                        .actionDrawingMenuFragmentToDrawingCanvasFragment()
                )
            }
        }

        binding.option2.setOnClickListener{
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.normalLinesTwo = true
                SharedPreferencesManager.drawingOption = 1
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    DrawingTestMenuFragmentDirections
                        .actionDrawingMenuFragmentToDrawingCanvasFragment()
                )
            }
        }

        binding.option3.setOnClickListener {
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.fadingLines = true
                SharedPreferencesManager.drawingOption = 2
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    DrawingTestMenuFragmentDirections
                        .actionDrawingMenuFragmentToDrawingCanvasFragment()
                )
            }
        }

        binding.option4.setOnClickListener {
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.fadingLinesTwo = true
                SharedPreferencesManager.drawingOption = 3
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    DrawingTestMenuFragmentDirections
                        .actionDrawingMenuFragmentToDrawingCanvasFragment())
            }
        }

        binding.option5.setOnClickListener {
            executeWithSound {
                binding.touch5.visibility = View.VISIBLE
                SharedPreferencesManager.stylusLines = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    DrawingTestMenuFragmentDirections
                        .actionDrawingMenuFragmentToStylusLinesFragment())
            }
        }

        binding.option6.setOnClickListener {
            executeWithSound {
                binding.touch6.visibility = View.VISIBLE
                SharedPreferencesManager.colorLines = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    DrawingTestMenuFragmentDirections
                        .actionDrawingMenuFragmentToColorLinesFragment())
            }
        }

    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.normalLines) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.normalLinesTwo) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fadingLines) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fadingLinesTwo) {
            binding.touch4.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.stylusLines) {
            binding.touch5.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.colorLines) {
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