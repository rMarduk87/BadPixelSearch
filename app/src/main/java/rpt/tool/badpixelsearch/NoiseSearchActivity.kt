package rpt.tool.badpixelsearch

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import androidx.core.graphics.createBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager


class NoiseSearchActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var timer: Timer? = null
    private val random: Random = Random()
    private var screenWidth = 0
    private var screenHeight: Int = 0
    private val pixelSize = 4 // Size of each "pixel" in dp
    private var useColor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        useColor = SharedPreferencesManager.typeNoiseColored

        // Make fullscreen and hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        // Create ImageView to draw on
        imageView = ImageView(this)
        imageView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContentView(imageView)
    }

    override fun onResume() {
        super.onResume()

        // Get screen dimensions
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        // Convert dp to pixels
        val scale = resources.displayMetrics.density
        val pixelSizePx = (pixelSize * scale + 0.5f).toInt()

        // Create timer to update pixels
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val pixels = IntArray(screenWidth * screenHeight)

                    for (i in pixels.indices) {
                        pixels[i] = if (useColor) {
                            // Colori casuali vivaci (HSV: saturazione e valore alti)
                            val hue = random.nextFloat() * 360f      // tonalità 0–360°
                            val saturation = 0.9f + random.nextFloat() * 0.1f // 0.9–1.0
                            val value = 0.9f + random.nextFloat() * 0.1f      // 0.9–1.0
                            Color.HSVToColor(floatArrayOf(hue, saturation, value))
                        } else {
                            if (random.nextBoolean()) Color.WHITE else Color.BLACK
                        }
                    }

                    // Crea bitmap e applica i pixel
                    val bitmap = createBitmap(screenWidth, screenHeight, Bitmap.Config.RGB_565)
                    bitmap.setPixels(pixels, 0, screenWidth, 0, 0, screenWidth, screenHeight)
                    imageView!!.setImageBitmap(bitmap)
                }
            }
        }, 0, 35) // Update ogni ~35ms (≈28 FPS)
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }
}