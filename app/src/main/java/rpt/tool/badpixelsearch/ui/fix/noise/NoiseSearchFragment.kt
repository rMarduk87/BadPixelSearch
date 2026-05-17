package rpt.tool.badpixelsearch.ui.fix.noise

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentNoiseSearchBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import java.util.*

class NoiseSearchFragment : BaseFragment<FragmentNoiseSearchBinding>(FragmentNoiseSearchBinding::inflate) {

    private var timer: Timer? = null
    private val random: Random = Random()
    private var screenWidth = 0
    private var screenHeight: Int = 0
    private var useColor = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useColor = SharedPreferencesManager.typeNoiseColored

        // Hide system bars for the test
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onResume() {
        super.onResume()

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    if (view == null) return@runOnUiThread
                    
                    val pixels = IntArray(screenWidth * screenHeight)
                    for (i in pixels.indices) {
                        pixels[i] = if (useColor) {
                            val hue = random.nextFloat() * 360f
                            val saturation = 0.9f + random.nextFloat() * 0.1f
                            val value = 0.9f + random.nextFloat() * 0.1f
                            Color.HSVToColor(floatArrayOf(hue, saturation, value))
                        } else {
                            if (random.nextBoolean()) Color.WHITE else Color.BLACK
                        }
                    }

                    val bitmap = createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565)
                    bitmap.setPixels(pixels, 0, screenWidth, 0, 0, screenWidth, screenHeight)
                    binding.noiseImageView.setImageBitmap(bitmap)
                }
            }
        }, 0, 35)
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restore system bars when leaving
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }
}