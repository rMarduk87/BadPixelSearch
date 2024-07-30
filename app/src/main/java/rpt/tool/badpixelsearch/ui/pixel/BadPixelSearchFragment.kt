package rpt.tool.badpixelsearch.ui.pixel

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
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.FixPixelActivity
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.BadPixelSearchFragmentBinding
import rpt.tool.badpixelsearch.utils.extensions.modeToText
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate
import kotlin.math.abs

@Suppress("DEPRECATION")
class BadPixelSearchFragment :
    BaseFragment<BadPixelSearchFragmentBinding>(BadPixelSearchFragmentBinding::inflate),
    View.OnClickListener {

    private var finalizer: Runnable? = null
    private val timeoutHandler = Handler()
    private var isRunning = false
    var interval = 0
    var count = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedPreferencesManager.firstRun = false

        val gestureDetector = GestureDetector(RptDetectGesture())

        j = 1
        interval = SharedPreferencesManager.interval

        binding.mainBG.setOnClickListener(this)
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        binding.backToMenuBtn.setOnClickListener {
            safeNavController?.safeNavigate(
                BadPixelSearchFragmentDirections.actionBadPixelSearchFragmentToMenuFragment())
        }

        binding.slideTv.text = requireContext().getString(R.string.slide).replace("$1",
            requireContext().getString(SharedPreferencesManager.mode.modeToText()))


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
        if (i > 8) i = 0
        if (i < 0) i = 8
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
            8 -> {
                binding.mainBG.setBackgroundColor(resources.getColor(R.color.white))
                isRunning = count <= interval && SharedPreferencesManager.mode == 1
            }
            else -> {}
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
                        startActivity(Intent(requireContext(), FixPixelActivity::class.java))
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
                        startActivity(Intent(requireContext(), FixPixelActivity::class.java))
                    }
                    else -> {
                        isRunning = false
                        timeoutHandler.removeCallbacks(finalizer!!)
                        i=0
                        changeColor()
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
                        startActivity(Intent(requireContext(), FixPixelActivity::class.java))
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
                        startActivity(Intent(requireContext(), FixPixelActivity::class.java))
                    }
                    else -> {
                        isRunning = false
                        timeoutHandler.removeCallbacks(finalizer!!)
                        i=0
                        changeColor()
                    }
                }

                return true
            }

            return false
        }
    }

    private fun automatic() {
        val delay = (if (SharedPreferencesManager.velocity == 0) 3000 else 2000).toLong()


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