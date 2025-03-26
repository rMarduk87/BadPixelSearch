package rpt.tool.badpixelsearch

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


class NoiseSearchActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var timer: Timer? = null
    private val random: Random = Random()
    private var screenWidth = 0
    private var screenHeight: Int = 0
    private val pixelSize = 4 // Size of each "pixel" in dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
                    // Create a new bitmap with random pixels
                    val pixels = IntArray(screenWidth * screenHeight)
                    for (i in pixels.indices) {
                        pixels[i] =
                            if (random.nextBoolean()) Color.WHITE else Color.BLACK
                    }


                    // Create bitmap and set it to ImageView
                    val bitmap = createBitmap(screenWidth, screenHeight)
                    bitmap.setPixels(pixels, 0, screenWidth, 0, 0, screenWidth, screenHeight)
                    imageView!!.setImageBitmap(bitmap)
                }
            }
        }, 0, 35) // Update every 100ms (10 FPS)
    }

    override fun onPause() {
        super.onPause()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }
}