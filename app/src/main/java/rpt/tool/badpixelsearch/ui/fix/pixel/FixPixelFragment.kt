package rpt.tool.badpixelsearch.ui.fix.pixel

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.FragmentFixPixelBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import java.util.*

class FixPixelFragment : BaseFragment<FragmentFixPixelBinding>(FragmentFixPixelBinding::inflate) {

    private var fixCancelled = false
    private var fixDelay = 100
    private var isFixing = false
    private val doublePressDelay = 500
    private var lastPressTime: Long = 0
    private val rnd = Random()
    private val autoHideTimeMillis = 5000
    private val uiAnimationDelay = 300
    private val mHideHandler = Handler(Looper.getMainLooper())
    private var mVisible = false

    private val mHidePart2Runnable = Runnable {
        binding.frameFix.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private val mShowPart2Runnable = Runnable {
        binding.fullscreenContentControls.visibility = View.VISIBLE
    }

    private val mHideRunnable = Runnable { hide() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, "Fix Pixel")
        mVisible = true

        binding.frameFix.setOnClickListener {
            val pressTime = System.currentTimeMillis()
            if (pressTime - lastPressTime > doublePressDelay) {
                if (!isFixing) {
                    val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                    binding.frameFix.setBackgroundColor(color)
                }
            } else {
                toggle()
            }
            lastPressTime = pressTime
        }

        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        binding.BtnCloseFix.setOnClickListener {
            fixCancelled = true
            safeNavController?.popBackStack()
        }

        fixDelay = SharedPreferencesManager.delay
        if (SharedPreferencesManager.action == "fix") {
            fix()
        }

        if (SharedPreferencesManager.fullBrightness) {
            val lp = window.attributes
            lp.screenBrightness = 1.0f
            window.attributes = lp
        }
        
        delayedHide(300)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        mHideHandler.removeCallbacksAndMessages(null)
    }

    private fun fix() {
        isFixing = true
        object : CountDownTimer(fixDelay.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (_binding == null) return
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                binding.frameFix.setBackgroundColor(color)
                if (!fixCancelled) {
                    start()
                }
            }
        }.start()
    }

    private fun start() {
        fix()
    }

    private fun toggle() {
        if (mVisible) hide() else show()
    }

    private fun hide() {
        binding.fullscreenContentControls.visibility = View.GONE
        mVisible = false
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, uiAnimationDelay.toLong())
    }

    private fun show() {
        binding.frameFix.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, uiAnimationDelay.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }
}