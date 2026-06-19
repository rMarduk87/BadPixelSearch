package rpt.tool.badpixelsearch.ui.fix.menu

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.TestsMenuEightBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.com.base.navigation.safeNavController
import rpt.com.base.navigation.safeNavigate

class FixPixelMenuFragment: BaseFragment<TestsMenuEightBinding>
    (TestsMenuEightBinding::inflate,false) {

    private val PULSE_DURATION = 2500L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(R.string.fix_tests))
        binding.iconAnimated.setImageResource(R.drawable.splash)

        binding.toolbar.btnInfo.visibility = View.VISIBLE

        binding.text1.text = requireContext().getString(R.string.bw_test)
        binding.text2.text = requireContext().getString(R.string.noise_test)
        binding.text3.text = requireContext().getString(R.string.snow)
        binding.text4.text = requireContext().getString(R.string.horizontal_line_test)
        binding.text5.text = requireContext().getString(R.string.vertical_line_test)
        binding.text6.text = requireContext().getString(R.string.hori_rect_test)
        binding.text7.text = requireContext().getString(R.string.vertical_rectangle_test)
        binding.text8.text = requireContext().getString(R.string.fix_test)

        setupNumbers()

        ObjectAnimator.ofFloat(binding.iconAnimated, "alpha",
            1f, 0f).apply {
            duration = PULSE_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        binding.option1.setOnClickListener {
            executeWithSound {
                binding.touch1.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestBW = true
                SharedPreferencesManager.typeMode = 1
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToBadPixelSearchFragment()
                )
            }
        }

        binding.option2.setOnClickListener {
            executeWithSound {
                binding.touch2.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestNoise = true
                SharedPreferencesManager.typeMode = 2
                SharedPreferencesManager.typeNoiseColored = false
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToNoiseSearchFragment()
                )
            }
        }

        binding.option3.setOnClickListener {
            executeWithSound {
                binding.touch3.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestSnow = true
                SharedPreferencesManager.typeMode = 2
                SharedPreferencesManager.typeNoiseColored = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToNoiseSearchFragment()
                )
            }
        }

        binding.option4.setOnClickListener {
            executeWithSound {
                binding.touch4.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestHorLine = true
                SharedPreferencesManager.typeMode = 3
                SharedPreferencesManager.isVertical = false
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToPixelTestFragment()
                )
            }
        }

        binding.option5.setOnClickListener {
            executeWithSound {
                binding.touch5.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestVerLine = true
                SharedPreferencesManager.typeMode = 4
                SharedPreferencesManager.isVertical = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToPixelTestFragment()
                )
            }
        }

        binding.option6.setOnClickListener {
            executeWithSound {
                binding.touch6.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestHorRect = true
                SharedPreferencesManager.typeMode = 5
                SharedPreferencesManager.isVertical = false
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToPixelTestFragment()
                )
            }
        }

        binding.option7.setOnClickListener {
            executeWithSound {
                binding.touch7.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestVerRect = true
                SharedPreferencesManager.typeMode = 6
                SharedPreferencesManager.isVertical = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToPixelTestFragment()
                )
            }
        }

        binding.option8.setOnClickListener {
            executeWithSound {
                binding.touch8.visibility = View.VISIBLE
                SharedPreferencesManager.fixTestFix = true
                safeNavController(R.id.main_activity_nav_host_fragment)?.safeNavigate(
                    FixPixelMenuFragmentDirections
                        .actionFixTestMenuFragmentToFixPixelFragment()
                )
            }
        }

        binding.toolbar.btnInfo.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun showInfoDialog() {
        val li = LayoutInflater.from(requireContext())
        val promptsView = li.inflate(R.layout.dialog_fix_pixel_info, null)

        val alertDialog = AlertDialog.Builder(requireContext(),
            R.style.alert_dialog)
            .setView(promptsView)
            .create()

        promptsView.findViewById<View>(R.id.btnClose).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.fixTestBW) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestNoise) {
            binding.touch2.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestSnow) {
            binding.touch3.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestHorLine) {
            binding.touch4.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestVerLine) {
            binding.touch5.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestHorRect) {
            binding.touch6.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestVerRect) {
            binding.touch7.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.fixTestFix) {
            binding.touch8.visibility = View.VISIBLE
        }
    }

    private fun executeWithSound(action: () -> Unit) {
        if (SharedPreferencesManager.sound) {
            try {
                val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.click_sound)
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
