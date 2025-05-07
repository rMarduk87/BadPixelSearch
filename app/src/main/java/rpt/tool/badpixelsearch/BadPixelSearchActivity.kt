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
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.databinding.ActivityBadPixelSearchBinding
import rpt.tool.badpixelsearch.utils.extensions.modeToText
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

        binding.mainBG.setOnClickListener(this)
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        binding.backToMenuBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.slideTv.text = getString(R.string.slide).replace("$1",
            getString(SharedPreferencesManager.mode.modeToText()))


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
                if(SharedPreferencesManager.mode == 0){
                    scrolling()
                }
            }

            else -> {}
        }
    }

    fun changeColor() {
        when (SharedPreferencesManager.typeMode) {
            0 -> {
                if (i > 9) i = 0
                if (i < 0) i = 9
                if (j > 0) {
                    binding.appname.visibility = View.GONE
                    binding.slideTv.visibility = View.GONE
                    binding.backToMenuBtn.visibility = View.GONE
                }
                when (i) {
                    0 -> start()
                    1 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.red))
                    2 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.green))
                    3 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.blue))
                    4 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.cyan))
                    5 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.magenta))
                    6 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.yellow))
                    7 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.grey))
                    8-> binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
                    9-> {
                        binding.mainBG.setBackgroundColor(resources.getColor(R.color.white))
                        isRunning = count <= interval && SharedPreferencesManager.mode == 1
                    }
                    else -> {}
                }
            }
            1 -> {
                if (i > 2) i = 0
                if (i < 0) i = 2
                if (j > 0) {
                    binding.appname.visibility = View.GONE
                    binding.slideTv.visibility = View.GONE
                    binding.backToMenuBtn.visibility = View.GONE
                }
                when (i) {
                    0 -> start()
                    1-> binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
                    2-> {
                        binding.mainBG.setBackgroundColor(resources.getColor(R.color.white))
                        isRunning = count <= interval && SharedPreferencesManager.mode == 1
                    }
                    else -> {}
                }
            }
            else -> {

            }
        }

    }

    private fun start() {
        if(!isRunning){
            binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
            binding.appname.visibility = View.VISIBLE
            binding.slideTv.visibility = View.VISIBLE
            binding.backToMenuBtn.visibility = View.VISIBLE
            if(finalizer != null){
                timeoutHandler.removeCallbacks(finalizer!!)
            }
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
                when (SharedPreferencesManager.mode) {
                    0 -> {
                        i++
                        changeColor()
                    }
                    2 -> {
                        startActivity(Intent(baseContext, FixPixelActivity::class.java))
                    }
                    else -> {
                        isRunning = true
                        automatic()
                    }
                }

                return true
            } else if (e2.x - e1.x > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
                when (SharedPreferencesManager.mode) {
                    0 -> {
                        i--
                        changeColor()
                    }
                    2 -> {
                        startActivity(Intent(baseContext, FixPixelActivity::class.java))
                    }
                    else -> {
                        isRunning = false
                        if(finalizer != null){
                            timeoutHandler.removeCallbacks(finalizer!!)
                            i=0
                            changeColor()
                        }
                    }
                }

                return true
            }
            if (e1.y - e2.y > swipeMinDistance && abs(velocityY) > swipeThresholdVelocity) {
                when (SharedPreferencesManager.mode) {
                    0 -> {
                        i++
                        changeColor()
                    }
                    2 -> {
                        startActivity(Intent(baseContext, FixPixelActivity::class.java))
                    }
                    else -> {
                        isRunning = true
                        automatic()
                    }
                }

                return true
            } else if (e2.y - e1.y > swipeMinDistance && abs(velocityY) > swipeThresholdVelocity) {
                when (SharedPreferencesManager.mode) {
                    0 -> {
                        i--
                        changeColor()
                    }
                    2 -> {
                        startActivity(Intent(baseContext, FixPixelActivity::class.java))
                    }
                    else -> {
                        isRunning = false
                        if(finalizer != null){
                            timeoutHandler.removeCallbacks(finalizer!!)
                            i=0
                            changeColor()
                        }
                    }
                }

                return true
            }

            return false
        }
    }

    private fun automatic() {
        var delay = (if (SharedPreferencesManager.velocity == 0) 3000 else 2000).toLong()
        if(SharedPreferencesManager.typeMode==1){
            delay /= 35
        }


        finalizer = object  : Runnable{
            override fun run() {
                if(isRunning){
                    count += delay.toInt()
                    if(count<= interval){
                        scrolling()
                        timeoutHandler.postDelayed(this, delay)//1 sec delay
                    }
                    else{
                        timeoutHandler.removeCallbacks(finalizer!!)
                        i=0
                        isRunning = false
                        count = 0
                        start()
                    }

                }
                else{
                    timeoutHandler.removeCallbacks(finalizer!!)
                    i=0
                    changeColor()
                }
            }
        }

        timeoutHandler.postDelayed(finalizer!!,0 )

    }
}