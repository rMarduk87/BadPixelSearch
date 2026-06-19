package rpt.tool.badpixelsearch.ui.fix.bars

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentPixelTestBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import java.util.*

class PixelTestFragment : BaseFragment<FragmentPixelTestBinding>(FragmentPixelTestBinding::inflate,false) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val pixelTestView = PixelTestView(requireContext(), SharedPreferencesManager.isVertical)
        binding.pixelTestContainer.addView(pixelTestView, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    @SuppressLint("ViewConstructor")
    class PixelTestView(context: Context, private val verticalBars: Boolean) :
        SurfaceView(context), SurfaceHolder.Callback {
        private val paint = Paint()
        private val colors = intArrayOf(
            -0x10000, -0xff0100, -0xffff01, -0xff0001, -0xff01,
            -0x100, -0x7f7f80, -0x1, -0x1000000, -0x74baed
        )
        private val random = Random()
        private val barWidth = if (SharedPreferencesManager.typeMode == 3 ||
            SharedPreferencesManager.typeMode == 4) 2 else 8
        private val updateInterval = (if (SharedPreferencesManager.velocity == 0) 50 else 25).toLong()
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
                    canvas.drawRect(x.toFloat(), 0f, (x + barWidth).toFloat(), height.toFloat(), paint)
                    x += barWidth
                }
            } else {
                var y = 0
                while (y < height) {
                    paint.color = colors[random.nextInt(colors.size)]
                    canvas.drawRect(0f, y.toFloat(), width.toFloat(), (y + barWidth).toFloat(), paint)
                    y += barWidth
                }
            }
            holder.unlockCanvasAndPost(canvas)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            running = true
            Thread {
                while (running) {
                    drawPattern()
                    try {
                        Thread.sleep(updateInterval)
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