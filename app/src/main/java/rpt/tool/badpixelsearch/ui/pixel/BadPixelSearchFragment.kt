package rpt.tool.badpixelsearch.ui.pixel

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.skydoves.balloon.BalloonAlign
import com.skydoves.balloon.balloon
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.FixPixelActivity
import rpt.tool.badpixelsearch.MainActivity
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.WalkThroughActivity
import rpt.tool.badpixelsearch.databinding.BadPixelSearchFragmentBinding
import rpt.tool.badpixelsearch.utils.balloon.HelpBalloonFactory
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
    private val helpBalloon by balloon<HelpBalloonFactory>()
    var interval = 0
    var count = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SharedPreferencesManager.firstRun) {
            startActivity(Intent(requireContext(), WalkThroughActivity::class.java))
        }

        val gestureDetector = GestureDetector(RptDetectGesture())

        j = 1
        interval = SharedPreferencesManager.interval

        binding.mainBG.setOnClickListener(this)
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        binding.sendRequestBtn.setOnClickListener {
            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.sender_dialog, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)

            val userInputTextSub: EditText = promptsView.findViewById(R.id.txtSub)

            val userInputTxtMsg: EditText = promptsView.findViewById(R.id.txtMsg)

            val userInputSendBtn: Button = promptsView.findViewById(R.id.btnSend)

            userInputSendBtn.setOnClickListener {
                val inputText = userInputTextSub.text.toString()
                val inputTextMsg = userInputTxtMsg.text.toString()
                sendMail(requireActivity(),
                    arrayOf(resources.getString(R.string.to)), inputText, inputTextMsg)
                userInputTxtMsg.text.clear()
            }

            alertDialogBuilder.setPositiveButton("OK") { _, _ ->

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        binding.openSettingsMenuBtn.setOnClickListener {
            safeNavController?.safeNavigate(
                BadPixelSearchFragmentDirections.actionBadPixelSearchFragmentToSettingsFragment())
        }

        binding.appname.setOnClickListener{
            Handler(Looper.getMainLooper()).postDelayed({
                helpBalloon.showAlign(
                    align = BalloonAlign.BOTTOM,
                    mainAnchor = binding.appname as View,
                    subAnchorList = listOf(it),
                )
            },1450)

            Handler(Looper.getMainLooper()).postDelayed({
                helpBalloon.dismiss()
            }, 10000)
        }
        count = 0
    }

    private fun scrolling() {
        i++
        changeColor()
    }

    private fun sendMail(
        activity: Activity,
        emailIds: Array<String>,
        subject: String,
        textMessage: String
    ) {


        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.setData(Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailIds)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
        emailIntent.setType("message/rfc822")
        try {
            activity.startActivity(
                Intent.createChooser(
                    emailIntent,
                    resources.getString(R.string.choose_mail_app)
                )
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                activity,
                ex.message,
                Toast.LENGTH_SHORT
            ).show()
        }
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
            binding.sendTv.visibility = View.GONE
            binding.sendRequestBtn.visibility = View.GONE
            binding.openSettingsMenuBtn.visibility = View.GONE
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
            binding.sendTv.visibility = View.VISIBLE
            binding.sendRequestBtn.visibility = View.VISIBLE
            binding.openSettingsMenuBtn.visibility = View.VISIBLE
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

        private val SWIPE_MIN_DISTANCE = 120
        private val SWIPE_THRESHOLD_VELOCITY = 50

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1!!.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if(SharedPreferencesManager.mode == 0){
                    i++
                    changeColor()
                }
                else if(SharedPreferencesManager.mode == 2){
                    startActivity(Intent(requireContext(), FixPixelActivity::class.java))
                }
                else{
                    isRunning = true
                    automatic()
                }

                return true
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if(SharedPreferencesManager.mode == 0){
                    i--
                    changeColor()
                }
                else if(SharedPreferencesManager.mode == 2){
                    startActivity(Intent(requireContext(), FixPixelActivity::class.java))
                }
                else{
                    isRunning = false
                    timeoutHandler.removeCallbacks(finalizer!!)
                    i=0
                    changeColor()
                }

                return true
            }
            if (e1.y - e2.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                if(SharedPreferencesManager.mode == 0){
                    i++
                    changeColor()
                }
                else if(SharedPreferencesManager.mode == 2){
                    startActivity(Intent(requireContext(), FixPixelActivity::class.java))
                }
                else{
                    isRunning = true
                    automatic()
                }

                return true
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                if(SharedPreferencesManager.mode == 0){
                    i--
                    changeColor()
                }
                else if(SharedPreferencesManager.mode == 2){
                    startActivity(Intent(requireContext(), FixPixelActivity::class.java))
                }
                else{
                    isRunning = false
                    timeoutHandler.removeCallbacks(finalizer!!)
                    i=0
                    changeColor()
                }

                return true
            }

            return false
        }
    }

    private fun automatic() {
        var delay = (if (SharedPreferencesManager.velocity == 0) 3000 else 2000).toLong()


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