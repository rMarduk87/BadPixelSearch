package rpt.tool.badpixelsearch

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import rpt.tool.badpixelsearch.databinding.ActivitySplashScreenBinding
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import rpt.tool.badpixelsearch.ui.animation.menu.AnimationMenuFragment.Companion.PULSE_DURATION

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private var time: Long = SHOW
    private val timeoutHandler = Handler()

    companion object {
        const val SHOW: Long = 1375
        const val PULSE_DURATION = 2000L
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        ObjectAnimator.ofFloat(binding.icon, "alpha", 1f,
            0f).apply {
            duration = PULSE_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        val finalizer = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        timeoutHandler.postDelayed(finalizer, time)


        binding.main.setOnClickListener{
            timeoutHandler.removeCallbacks(finalizer)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}