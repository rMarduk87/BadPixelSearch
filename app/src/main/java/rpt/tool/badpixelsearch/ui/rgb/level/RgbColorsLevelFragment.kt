package rpt.tool.badpixelsearch.ui.rgb.level

import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentRgbColorsLevelBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager

class RgbColorsLevelFragment :
    BaseFragment<FragmentRgbColorsLevelBinding>(FragmentRgbColorsLevelBinding::inflate,false) {

    private var r = 128
    private var g = 0
    private var b = 0
    private var l = 102
    private var mode = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.cardMixer.setRenderEffect(
                RenderEffect.createBlurEffect(40f, 40f, Shader.TileMode.CLAMP)
            )
        }

        mode = SharedPreferencesManager.rgbOption
        setupMode()
        setupSeekbars()
        updateUI()

        // Hide system bars for the test
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restore system bars when leaving
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    private fun setupMode() {
        var title = ""
        when (mode) {
            0 -> { // RED
                r = 128; g = 0; b = 0; l = 102
                title = getString(R.string.red_level_act)
                enableSeekbars(rEnabled = true, gEnabled = false, bEnabled = false, lEnabled = true)
            }
            1 -> { // GREEN
                r = 0; g = 128; b = 0; l = 102
                title = getString(R.string.green_level_act)
                enableSeekbars(rEnabled = false, gEnabled = true, bEnabled = false, lEnabled = true)
            }
            2 -> { // BLUE
                r = 0; g = 0; b = 128; l = 102
                title = getString(R.string.blue_level_act)
                enableSeekbars(rEnabled = false, gEnabled = false, bEnabled = true, lEnabled = true)
            }
            3 -> { // GRAY
                r = 128; g = 128; b = 128; l = 102
                title = getString(R.string.gray_level_act)
                enableSeekbars(true, true, true, true)
            }
            4 -> { // ALL COLORS
                r = 128; g = 128; b = 128; l = 102
                title = getString(R.string.all_colors_act)
                enableSeekbars(true, true, true, true)
            }
        }
        
        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, title)

        binding.seekR.progress = r
        binding.seekG.progress = g
        binding.seekB.progress = b
        binding.seekL.progress = l
    }

    private fun enableSeekbars(rEnabled: Boolean, gEnabled: Boolean, bEnabled: Boolean, lEnabled: Boolean) {
        binding.seekR.isEnabled = rEnabled
        binding.seekG.isEnabled = gEnabled
        binding.seekB.isEnabled = bEnabled
        binding.seekL.isEnabled = lEnabled
    }

    private fun setupSeekbars() {
        binding.seekR.setOnSeekBarChangeListener(simpleSeek { value ->
            r = value
            if (mode == 3) {
                g = value; b = value
                binding.seekG.progress = value
                binding.seekB.progress = value
            }
            updateUI()
        })

        binding.seekG.setOnSeekBarChangeListener(simpleSeek { value ->
            g = value
            if (mode == 3) {
                r = value; b = value
                binding.seekR.progress = value
                binding.seekB.progress = value
            }
            updateUI()
        })

        binding.seekB.setOnSeekBarChangeListener(simpleSeek { value ->
            b = value
            if (mode == 3) {
                r = value; g = value
                binding.seekR.progress = value
                binding.seekG.progress = value
            }
            updateUI()
        })

        binding.seekL.setOnSeekBarChangeListener(simpleSeek { value ->
            l = value
            updateUI()
        })
    }

    private fun simpleSeek(onChange: (Int) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                onChange(value)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

    private fun updateUI() {
        val luminance = l / 255f
        val finalR = (r * luminance).toInt()
        val finalG = (g * luminance).toInt()
        val finalB = (b * luminance).toInt()

        binding.rootLayout.setBackgroundColor(Color.rgb(finalR, finalG, finalB))

        binding.lblR.text = getString(R.string.r) + " " + r
        binding.lblG.text = getString(R.string.g) + " " + g
        binding.lblB.text = getString(R.string.b) + " " + b
        binding.lblL.text = getString(R.string.l) + " " + l
    }
}