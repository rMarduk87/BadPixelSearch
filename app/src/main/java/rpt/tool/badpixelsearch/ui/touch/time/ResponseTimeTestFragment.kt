package rpt.tool.badpixelsearch.ui.touch.time

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentResponseTimeTestBinding

class ResponseTimeTestFragment : BaseFragment<FragmentResponseTimeTestBinding>(
    FragmentResponseTimeTestBinding::inflate
) {

    private val pointerCircleMap = mutableMapOf<Int, View>()
    private var circleSizePx = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.quick_taps)
        )

        circleSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            120f,
            resources.displayMetrics
        ).toInt()

        binding.mainLayout.setOnTouchListener { _, event ->
            val action = event.actionMasked
            val pointerIndex = event.actionIndex
            val pointerId = event.getPointerId(pointerIndex)

            when (action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val latenza = SystemClock.uptimeMillis() - event.eventTime
                    val testoLatenza = if (latenza > 100) getString(R.string.more_than_100ms) else getString(R.string.ms_format, latenza)

                    binding.toolbar.menuTitle.text = testoLatenza

                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)

                    val circle = createCircleView()

                    val posX = x - (circleSizePx / 2f)
                    val posY = y - (circleSizePx / 2f)

                    binding.mainLayout.addView(circle)

                    val params = circle.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.TOP or Gravity.START
                    params.leftMargin = posX.toInt()
                    params.topMargin = posY.toInt()
                    circle.layoutParams = params

                    pointerCircleMap[pointerId] = circle
                }
                MotionEvent.ACTION_MOVE -> {

                    for (i in 0 until event.pointerCount) {
                        val id = event.getPointerId(i)
                        val circleView = pointerCircleMap[id]
                        circleView?.let {
                            val newX = event.getX(i) - (circleSizePx / 2f)
                            val newY = event.getY(i) - (circleSizePx / 2f)

                            val params = it.layoutParams as FrameLayout.LayoutParams
                            params.leftMargin = newX.toInt()
                            params.topMargin = newY.toInt()
                            it.layoutParams = params
                        }
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    pointerCircleMap.remove(pointerId)?.let {
                        binding.mainLayout.removeView(it)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    for (circleView in pointerCircleMap.values) {
                        binding.mainLayout.removeView(circleView)
                    }
                    pointerCircleMap.clear()
                    binding.toolbar.menuTitle.text = getString(R.string.zero_ms)
                }
            }
            true
        }
    }

    private fun createCircleView(): ImageView {
        val circleView = ImageView(requireContext())
        circleView.setImageResource(R.drawable.circle_feedback)

        val layoutParams = FrameLayout.LayoutParams(circleSizePx, circleSizePx)

        layoutParams.gravity = Gravity.TOP or Gravity.START
        circleView.layoutParams = layoutParams

        return circleView
    }
}