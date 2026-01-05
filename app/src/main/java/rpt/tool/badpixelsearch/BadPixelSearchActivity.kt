package rpt.tool.badpixelsearch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.databinding.ActivityBadPixelSearchBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import kotlin.math.abs

class BadPixelSearchActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityBadPixelSearchBinding
    private var finalizer: Runnable? = null
    private val timeoutHandler = Handler()
    private var isRunning = false
    var interval = 0
    var count = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBadPixelSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesManager.firstRun = false

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val gestureDetector = GestureDetector(RptDetectGesture())

        j = 1
        interval = SharedPreferencesManager.interval

        // Click e touch
        binding.mainBG.setOnClickListener(this)
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        count = 0
    }

    private fun scrolling() {
        i++
        changeColor()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mainBG -> {
                when (SharedPreferencesManager.mode) {
                    0 -> { // modalità colori
                        i++ // simula swipe verso sinistra
                        changeColor()
                    }
                    2 -> { // modalità FixPixel
                        startActivity(Intent(baseContext, FixPixelActivity::class.java))
                    }
                    else -> { // modalità automatica
                        isRunning = !isRunning
                        if (isRunning) automatic()
                        else {
                            if (finalizer != null) {
                                timeoutHandler.removeCallbacks(finalizer!!)
                                i = 0
                                changeColor()
                                count = 0
                            }
                        }
                    }
                }
            }
        }
    }

    fun changeColor() {
        when (SharedPreferencesManager.typeMode) {
            0 -> {
                if (i > 9) i = 0
                if (i < 0) i = 9
                when (i) {
                    0 -> start()
                    1 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.red))
                    2 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.green))
                    3 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.blue))
                    4 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.cyan))
                    5 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.magenta))
                    6 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.yellow))
                    7 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.gray))
                    8 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
                    9 -> {
                        binding.mainBG.setBackgroundColor(resources.getColor(R.color.white))
                        isRunning = count <= interval && SharedPreferencesManager.mode == 1
                    }
                }
            }
            1 -> {
                if (i > 2) i = 0
                if (i < 0) i = 2
                when (i) {
                    0 -> start()
                    1 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
                    2 -> {
                        binding.mainBG.setBackgroundColor(resources.getColor(R.color.white))
                        isRunning = count <= interval && SharedPreferencesManager.mode == 1
                    }
                }
            }
        }
    }

    private fun start() {
        if (!isRunning) {
            binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
            if (finalizer != null) {
                timeoutHandler.removeCallbacks(finalizer!!)
            }
        }
    }

    companion object {
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
            if (e1 == null) return false

            val horizontalSwipe = abs(e1.x - e2.x) > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity
            val verticalSwipe = abs(e1.y - e2.y) > swipeMinDistance && abs(velocityY) > swipeThresholdVelocity

            if (horizontalSwipe || verticalSwipe) {
                val increase = ( (e1.x - e2.x > 0) || (e1.y - e2.y > 0) )
                when (SharedPreferencesManager.mode) {
                    0 -> {
                        if (increase) i++ else i--
                        changeColor()
                    }
                    2 -> startActivity(Intent(baseContext, FixPixelActivity::class.java))
                    else -> {
                        isRunning = increase
                        automatic()
                    }
                }
                return true
            }
            return false
        }
    }

    private fun automatic() {
        var delay = (if (SharedPreferencesManager.velocity == 0) 3000 else 2000).toLong()
        if (SharedPreferencesManager.typeMode == 1) delay /= 35

        finalizer = object : Runnable {
            override fun run() {
                if (isRunning) {
                    count += delay.toInt()
                    if (count <= interval) {
                        scrolling()
                        timeoutHandler.postDelayed(this, delay)
                    } else {
                        timeoutHandler.removeCallbacks(finalizer!!)
                        i = 0
                        isRunning = false
                        count = 0
                        start()
                    }
                } else {
                    timeoutHandler.removeCallbacks(finalizer!!)
                    i = 0
                    changeColor()
                }
            }
        }

        timeoutHandler.postDelayed(finalizer!!, 0)
    }
}