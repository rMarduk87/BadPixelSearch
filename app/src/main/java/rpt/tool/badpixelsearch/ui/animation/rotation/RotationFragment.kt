package rpt.tool.badpixelsearch.ui.animation.rotation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentRotationBinding

class RotationFragment : BaseFragment<FragmentRotationBinding>(FragmentRotationBinding::inflate) {
    private lateinit var square1: View
    private lateinit var square2: View
    private lateinit var square3: View
    private lateinit var txtRotation: TextView

    private var lastFrameTime = 0L
    private var frameCount = 0
    private var fps = 0

    private val handler = Handler(Looper.getMainLooper())
    private val fpsRunnable = object : Runnable {
        override fun run() {
            fps = frameCount
            txtRotation.text = buildString {
                append(requireContext().getString(R.string.fps))
                append(" ")
                append(fps)
            }
            frameCount = 0
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rotation, container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        square1 = view.findViewById(R.id.square1)
        square2 = view.findViewById(R.id.square2)
        square3 = view.findViewById(R.id.square3)
        txtRotation = view.findViewById(R.id.txtRotation)

        startRotation(square1, 2000)
        startRotation(square2, 1600)
        startRotation(square3, 1200)

        lastFrameTime = System.nanoTime()
        startFpsCounter()
    }

    private fun startRotation(target: View, duration: Long) {
        val animator = ObjectAnimator.ofFloat(target, "rotation", 0f, 360f)
        animator.duration = duration
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()

        animator.addUpdateListener {
            frameCount++
        }

        animator.start()
    }

    private fun startFpsCounter() {
        handler.post(fpsRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(fpsRunnable)
    }

}