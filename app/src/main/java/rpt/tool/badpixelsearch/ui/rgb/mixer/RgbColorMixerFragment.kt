package rpt.tool.badpixelsearch.ui.rgb.mixer

import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.FragmentRgbColorMixerBinding

class RgbColorMixerFragment:
    BaseFragment<FragmentRgbColorMixerBinding>(FragmentRgbColorMixerBinding::inflate) {

    private var r = 128
    private var g = 128
    private var b = 128
    private var l = 102

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.cardMixer.setRenderEffect(
                RenderEffect.createBlurEffect(40f, 40f,
                    Shader.TileMode.CLAMP)
            )
        }

        setupSeekbars()
        updateAllBars()
    }

    // ------------------------------------------------------------
    // CONFIGURAZIONE SEEKBARS
    // ------------------------------------------------------------
    private fun setupSeekbars() {

        binding.seekR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                r = value
                binding.lblR.text = "R: $value"
                updateAllBars()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                g = value
                binding.lblG.text = "G: $value"
                updateAllBars()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                b = value
                binding.lblB.text = "B: $value"
                updateAllBars()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekL.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                l = value
                binding.lblL.text = "L: $value"
                updateAllBars()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    // ------------------------------------------------------------
    // AGGIORNAMENTO DI TUTTE LE 7 BANDE
    // ------------------------------------------------------------
    private fun updateAllBars() {
        binding.bar1.setBackgroundColor(applyL(r, 0, 0))           // R
        binding.bar2.setBackgroundColor(applyL(r, g, 0))          // R+G
        binding.bar3.setBackgroundColor(applyL(0, g, 0))           // G
        binding.bar4.setBackgroundColor(applyL(0, g, b))          // G+B
        binding.bar5.setBackgroundColor(applyL(0, 0, b))           // B
        binding.bar6.setBackgroundColor(applyL(r, 0, b))          // B+R
        binding.bar7.setBackgroundColor(applyL(r, g, b))         // R+G+B
    }

    // ------------------------------------------------------------
    // APPLICA LUMINOSITÀ (L) A UN COLORE RGB
    // ------------------------------------------------------------
    private fun applyL(r: Int, g: Int, b: Int): Int {
        // l = 0 → nero, 255 = colore pieno
        val factor = l / 255f
        val nr = (r * factor).toInt().coerceIn(0, 255)
        val ng = (g * factor).toInt().coerceIn(0, 255)
        val nb = (b * factor).toInt().coerceIn(0, 255)
        return Color.rgb(nr, ng, nb)
    }
}