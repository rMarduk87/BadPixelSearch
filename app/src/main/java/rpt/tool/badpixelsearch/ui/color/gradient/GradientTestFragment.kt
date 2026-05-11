package rpt.tool.badpixelsearch.ui.color.gradient

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentGradientTestBinding
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import kotlin.math.abs

class GradientTestFragment :
    BaseFragment<FragmentGradientTestBinding>(FragmentGradientTestBinding::inflate) {

    private var i = 1

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(binding.toolbar.btnBack, binding.toolbar.menuTitle, getString(R.string.gradient))

        // Hide system bars for the test
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        binding.mainBG.setOnClickListener {
            scrolling()
        }

        val gestureDetector = GestureDetector(requireContext(),
            RptDetectGesture())
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
        
        changeGradient()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restore system bars when leaving
        val window = requireActivity().window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    private fun scrolling() {
        i++
        changeGradient()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun modify(gradient: Int, text: String) {
        binding.mainBG.background = resources.getDrawable(gradient, null)
        binding.toolbar.menuTitle.text = text
    }

    private fun changeGradient() {
        if (i > 7) {
            safeNavController?.popBackStack()
            return
        }
        if (i < 1) i = 7
        when (i) {
            1 -> modify(R.drawable.white_gradient, getString(R.string.white))
            2 -> modify(R.drawable.red_gradient, getString(R.string.red))
            3 -> modify(R.drawable.green_gradient, getString(R.string.green))
            4 -> modify(R.drawable.blue_gradient, getString(R.string.blue))
            5 -> modify(R.drawable.magenta_gradient, getString(R.string.magenta))
            6 -> modify(R.drawable.cyan_gradient, getString(R.string.cyan))
            7 -> modify(R.drawable.yellow_gradient, getString(R.string.yellow))
        }
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
            if (e1.x - e2.x > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
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
            } else if (e2.y - e1.y > swipeMinDistance && abs(velocityY) >
                swipeThresholdVelocity) {
                i--
                changeGradient()
                return true
            }
            return false
        }
    }
}