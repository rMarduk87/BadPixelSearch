package rpt.tool.badpixelsearch

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import java.util.Random


class PixelTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        setContentView(PixelTestView(this, SharedPreferencesManager.isVertical))
    }

    @SuppressLint("ViewConstructor")
    class PixelTestView(context: PixelTestActivity?, private val verticalBars: Boolean) :
        SurfaceView(context), SurfaceHolder.Callback {
        private val paint = Paint()
        private val colors = intArrayOf(
            -0x10000,  // Rosso
            -0xff0100,  // Verde
            -0xffff01,  // Blu
            -0xff0001,  // Ciano
            -0xff01,  // Viola
            -0x100,  // Giallo
            -0x7f7f80,  // Grigio
            -0x1,  // Bianco
            -0x1000000,  // Nero
            -0x74baed // Marrone
        )
        private val random = Random()
        private val barWidth =
            if(SharedPreferencesManager.typeMode == 3 || SharedPreferencesManager.typeMode == 4) 2
            else 8
        private val updateInterval = (if (SharedPreferencesManager.velocity == 0) 50 else 25)
            .toLong() // Intervallo di aggiornamento in millisecondi
        private var running = true

        init {
            holder.addCallback(this)
        }

        private fun drawPattern() {
            val canvas = holder.lockCanvas() ?: return

            val width = width
            val height = height

            if (verticalBars) {
                var x = 0
                while (x < width) {
                    paint.color = colors[random.nextInt(colors.size)]
                    canvas.drawRect(
                        x.toFloat(),
                        0f,
                        (x + barWidth).toFloat(),
                        height.toFloat(),
                        paint
                    )
                    x += barWidth
                }
            } else {
                var y = 0
                while (y < height) {
                    paint.color = colors[random.nextInt(colors.size)]
                    canvas.drawRect(
                        0f,
                        y.toFloat(),
                        width.toFloat(),
                        (y + barWidth).toFloat(),
                        paint
                    )
                    y += barWidth
                }
            }

            holder.unlockCanvasAndPost(canvas)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            Thread {
                while (running) {
                    drawPattern()
                    try {
                        Thread.sleep(updateInterval.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            running = false
        }
    }
}