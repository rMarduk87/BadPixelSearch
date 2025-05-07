package rpt.tool.badpixelsearch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.BadPixelSearchActivity.Companion.i
import rpt.tool.badpixelsearch.BadPixelSearchActivity.Companion.j
import rpt.tool.badpixelsearch.databinding.ActivityGradientTestBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import kotlin.math.abs

class GradientTestActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityGradientTestBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradientTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        j = 1
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        binding.mainBG.setOnClickListener(this)

        val gestureDetector = GestureDetector(RptDetectGesture())

        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

    }

    private fun scrolling() {
        i++
        changeGradient()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mainBG -> {
                scrolling()
            }

            else -> {}
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun modify(gradient: Int, text: String) {
        binding.mainBG.background = resources.getDrawable(gradient,null)
        binding.lblColor.text = text
    }

    private fun changeGradient() {
        if (i > 7) startActivity(Intent(this,MainActivity::class.java))
        if (i < 0) i = 7
        when (i) {
            1 -> modify(R.drawable.white_gradient,getString(R.string.white))
            2 -> modify(R.drawable.red_gradient,getString(R.string.red))
            3 -> modify(R.drawable.green_gradient,getString(R.string.green))
            4 -> modify(R.drawable.blue_gradient,getString(R.string.blue))
            5 -> modify(R.drawable.magenta_gradient,getString(R.string.magenta))
            6 -> modify(R.drawable.cyan_gradient,getString(R.string.cyan))
            7 -> modify(R.drawable.yellow_gradient,getString(R.string.yellow))


            else -> {}
        }

    }

    companion object{
        var i = 0
        var j = 0
    }

    private inner class RptDetectGesture : SimpleOnGestureListener() {

        private val swipeMinDistance = 120
        private val swipeThresholdVelocity = 50

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1!!.x - e2.x > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
                i++
                changeGradient()

                return true
            } else if (e2.x - e1.x > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
                i--
                changeGradient()

                return true
            }
            if (e1.y - e2.y > swipeMinDistance && abs(velocityY) > swipeThresholdVelocity) {
                i++
                changeGradient()

                return true
            } else if (e2.y - e1.y > swipeMinDistance && abs(velocityY) > swipeThresholdVelocity) {
                i--
                changeGradient()

                return true
            }

            return false
        }
    }
}