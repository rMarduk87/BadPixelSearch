package rpt.tool.badpixelsearch.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.WalkThroughActivity
import rpt.tool.badpixelsearch.databinding.BadPixelSearchFragmentBinding
import rpt.tool.badpixelsearch.utils.AppUtils
import kotlin.math.abs


class BadPixelSearchFragment :
    BaseFragment<BadPixelSearchFragmentBinding>(BadPixelSearchFragmentBinding::inflate),
    View.OnClickListener {

    private var mMaxBrightness: Boolean = false
    private lateinit var sharedPref: SharedPreferences

    var start = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(AppUtils.USERS_SHARED_PREF,
            AppUtils.PRIVATE_MODE)

        if (sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true)) {
            startActivity(Intent(requireContext(), WalkThroughActivity::class.java))
        }

        val versionCode: Int = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionCode
        val versionName: String = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName
        val gestureDetector = GestureDetector(RptDetectGesture())

        binding.mainBG.setOnClickListener(this)
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        binding.appversion.text =
            getString(R.string.version) + " " + versionName +
                    " (" + getString(R.string.build) + " " + versionCode + ")"
        j = 1

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mainBG -> {
                i++
                changeColor()
            }

            else -> {}
        }
    }

    fun changeColor() {
        if (i > 7) i = 0
        if (i < 0) i = 7
        if (j > 0) {
            binding.appname.visibility = View.GONE
            binding.appinfo.visibility = View.GONE
            binding.appversion.visibility = View.GONE
        }
        when (i) {
            0 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.black))
            1 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.red))
            2 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.green))
            3 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.blue))
            4 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.cyan))
            5 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.magenta))
            6 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.yellow))
            7 -> binding.mainBG.setBackgroundColor(resources.getColor(R.color.white))
            else -> {}
        }
    }

    companion object{
        var i = 0
        var j = 0
    }



    private inner class RptDetectGesture : SimpleOnGestureListener() {

        private val SWIPE_MIN_DISTANCE = 120
        private val SWIPE_THRESHOLD_VELOCITY = 50

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1!!.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                i++
                changeColor()
                return true
            } else if (e2.x - e1!!.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                i--
                changeColor()
                return true
            }
            if (e1!!.y - e2.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                i++
                changeColor()
                return true
            } else if (e2.y - e1!!.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                i--
                changeColor()
                return true
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            toggleMaxBrightness();
            AppUtils.setFullBrightness(requireActivity().window, mMaxBrightness);
            return true
        }
    }

    private fun toggleMaxBrightness() {
        mMaxBrightness = !mMaxBrightness;
    }
}