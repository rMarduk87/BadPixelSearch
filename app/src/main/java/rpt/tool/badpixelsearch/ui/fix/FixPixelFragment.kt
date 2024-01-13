package rpt.tool.badpixelsearch.ui.fix

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.view.WindowManager
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FixPixelFragmentBinding
import rpt.tool.badpixelsearch.utils.AppUtils
import java.util.Random


class FixPixelFragment : BaseFragment<FixPixelFragmentBinding>(FixPixelFragmentBinding::inflate) {

    private lateinit var sharedPref: SharedPreferences
    private var fixCancelled = false
    private var fixDelay = 100

    private var isFixing = false
    private val doublePressDelay = 500
    private var lastPressTime: Long = 0

    private val rnd: Random = Random()
    private val AUTO_HIDE_DELAY_MILLIS = 5000
    private val UI_ANIMATION_DELAY = 300
    private val mHideHandler: Handler = Handler()
    private val mHidePart2Runnable = Runnable {
        binding.frameFix!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private val mShowPart2Runnable = Runnable {
        binding.fullscreenContentControls!!.visibility = View.VISIBLE
    }
    private var mVisible = false
    private val mHideRunnable = Runnable { hide() }
    private val mDelayHideTouchListener =
        OnTouchListener { _, _ ->
            fixCancelled = true
            false
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences(
            AppUtils.USERS_SHARED_PREF,
            AppUtils.PRIVATE_MODE)
        mVisible = true
        binding.frameFix!!.setOnClickListener { _: View? ->
            val pressTime = System.currentTimeMillis()
            if (pressTime - lastPressTime > doublePressDelay) {
                if (!isFixing) {
                    val color = Color.argb(
                        255,
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        rnd.nextInt(256)
                    )
                    binding.frameFix!!.setBackgroundColor(color)
                }
            } else {
                toggle()
            }
            lastPressTime = pressTime
        }
        binding.BtnCloseFix.setOnTouchListener(mDelayHideTouchListener)
        fixDelay = sharedPref.getInt(AppUtils.DELAY_KEY, 100)
        if (sharedPref.getInt(AppUtils.COLOR_KEY, -10) != -10) {
            binding.frameFix!!.setBackgroundColor(sharedPref.getInt(AppUtils.COLOR_KEY, 0))
        }
        if (sharedPref.getString(AppUtils.ACTION_KEY,"fix") == "fix") {
            fix()
        }
        val lp: WindowManager.LayoutParams = requireActivity().window.attributes
        lp.screenBrightness = 1.0f
        requireActivity().window.attributes = lp
    }

    private fun fix() {
        isFixing = true
        object : CountDownTimer(fixDelay.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                binding.frameFix!!.setBackgroundColor(color)
                if (!fixCancelled) {
                    start()
                }
            }
        }.start()
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
    }

    private fun hide() {
        binding.fullscreenContentControls!!.visibility = GONE
        mVisible = false
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        binding.frameFix!!.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }
}