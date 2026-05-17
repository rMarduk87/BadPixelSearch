package rpt.tool.badpixelsearch.ui.touch.multi

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentMultiTouchTestBinding

class MultiTouchTestFragment :
    BaseFragment<FragmentMultiTouchTestBinding>(FragmentMultiTouchTestBinding::inflate) {

    private val pointerCircleMap = mutableMapOf<Int, View>()
    private var circleWidth = 0
    private var circleHeight = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.touch_the_screen)
        )

        val circleDrawable = ContextCompat.getDrawable(requireContext(),
            R.drawable.circle_feedback)
        circleWidth = circleDrawable?.intrinsicWidth ?: 0
        circleHeight = circleDrawable?.intrinsicHeight ?: 0

        binding.mainLayout.setOnTouchListener { _, event ->
            val pointerCount = event.pointerCount
            val action = event.actionMasked
            val pointerIndex = event.actionIndex
            val pointerId = event.getPointerId(pointerIndex)

            when (action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)
                    val circle = createCircleView(x, y)
                    binding.mainLayout.addView(circle)
                    pointerCircleMap[pointerId] = circle
                    updateTitle(pointerCount)
                }
                MotionEvent.ACTION_MOVE -> {
                    for (i in 0 until event.pointerCount) {
                        val id = event.getPointerId(i)
                        val circleView = pointerCircleMap[id]
                        circleView?.let {
                            it.translationX = event.getX(i) - circleWidth / 2
                            it.translationY = event.getY(i) - circleHeight / 2
                        }
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    pointerCircleMap.remove(pointerId)?.let {
                        binding.mainLayout.removeView(it)
                    }
                    updateTitle(pointerCount - 1)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    for (circleView in pointerCircleMap.values) {
                        binding.mainLayout.removeView(circleView)
                    }
                    pointerCircleMap.clear()
                    updateTitle(0)
                }
            }
            true
        }
    }

    private fun updateTitle(count: Int) {
        val testo = if (count == 0) getString(R.string.fingers_0) else
            getString(R.string.fingers) + count
        binding.toolbar.menuTitle.text = testo
    }

    private fun createCircleView(x: Float, y: Float): View {
        val circleView = ImageView(requireContext())
        circleView.setImageResource(R.drawable.circle_feedback)

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        circleView.layoutParams = layoutParams
        circleView.translationX = x - circleWidth / 2
        circleView.translationY = y - circleHeight / 2

        return circleView
    }
}