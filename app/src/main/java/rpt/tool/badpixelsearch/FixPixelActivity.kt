package rpt.tool.badpixelsearch

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.databinding.ActivityFixPixelBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import java.util.Random


@Suppress("DEPRECATION")
class FixPixelActivity : AppCompatActivity() {
    private var fixCancelled = false
    private var frameLayout: FrameLayout? = null
    private var fixDelay = 100

    private var isFixing = false
    private val doublePressDelay = 500
    private var lastPressTime: Long = 0

    private val rnd = Random()
    private val autoHideTimeMillis = 5000
    private val uiAnimationDelay = 300
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        frameLayout!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    private var mControlsView: View? = null
    private val mShowPart2Runnable = Runnable {
        val actionBar = supportActionBar
        actionBar?.show()
        mControlsView!!.visibility = View.VISIBLE
    }
    private var mVisible = false
    private val mHideRunnable = Runnable { hide() }
    @SuppressLint("ClickableViewAccessibility")
    private val mDelayHideTouchListener =
        OnTouchListener { _, _ ->
            fixCancelled = true
            finish()
            false
        }

    private lateinit var binding: ActivityFixPixelBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixPixelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mVisible = true
        mControlsView = findViewById(R.id.fullscreen_content_controls)
        frameLayout = binding.frameFix
        frameLayout!!.setOnClickListener{ _: View? ->
            val pressTime = System.currentTimeMillis()
            if (pressTime - lastPressTime > doublePressDelay) {
                if (!isFixing) {
                    val color = Color.argb(
                        255,
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        rnd.nextInt(256)
                    )
                    frameLayout!!.setBackgroundColor(color)
                }
            } else {
                toggle()
            }
            lastPressTime = pressTime
        }
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        binding.BtnCloseFix.setOnTouchListener(mDelayHideTouchListener)
        fixDelay = SharedPreferencesManager.delay
        if (SharedPreferencesManager.action == "fix") {
            fix()
        }
        if (SharedPreferencesManager.fullBrightness) {
            val lp = window.attributes
            lp.screenBrightness = 1.0f
            window.attributes = lp
        }

        val attrs = window.attributes
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.attributes = attrs
    }

    private fun fix() {
        isFixing = true
        object : CountDownTimer(fixDelay.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                    rnd.nextInt(256))
                frameLayout!!.setBackgroundColor(color)
                if (!fixCancelled) {
                    start()
                }
            }
        }.start()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(300)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
            delayedHide(autoHideTimeMillis)
        }
    }

    private fun hide() {
        val actionBar = supportActionBar
        actionBar?.hide()
        mControlsView!!.visibility = View.GONE
        mVisible = false
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, uiAnimationDelay.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        frameLayout!!.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, uiAnimationDelay.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}