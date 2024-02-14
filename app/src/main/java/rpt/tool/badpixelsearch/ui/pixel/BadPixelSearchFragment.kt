package rpt.tool.badpixelsearch.ui.pixel

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.WalkThroughActivity
import rpt.tool.badpixelsearch.databinding.BadPixelSearchFragmentBinding
import rpt.tool.badpixelsearch.utils.AppUtils
import kotlin.math.abs


class BadPixelSearchFragment :
    BaseFragment<BadPixelSearchFragmentBinding>(BadPixelSearchFragmentBinding::inflate),
    View.OnClickListener {

    private lateinit var sharedPref: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(AppUtils.USERS_SHARED_PREF,
            AppUtils.PRIVATE_MODE)

        if (sharedPref.getBoolean(AppUtils.FIRST_RUN_KEY, true)) {
            startActivity(Intent(requireContext(), WalkThroughActivity::class.java))
        }

        val gestureDetector = GestureDetector(RptDetectGesture())

        binding.mainBG.setOnClickListener(this)
        binding.mainBG.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
        j = 1

        binding.sendRequestBtn.setOnClickListener {
            val li = LayoutInflater.from(requireContext())
            val promptsView = li.inflate(R.layout.sender_dialog, null)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(promptsView)

            val userInputTextSub = promptsView.findViewById(R.id.txtSub) as EditText

            val userInputTxtMsg = promptsView.findViewById(R.id.txtMsg) as EditText

            val userInputSendBtn = promptsView.findViewById(R.id.btnSend) as Button

            userInputSendBtn.setOnClickListener {
                val inputText = userInputTextSub.text.toString()
                val inputTextMsg = userInputTxtMsg.text.toString()
                sendMail(requireActivity(),
                    arrayOf(resources.getString(R.string.to)), inputText, inputTextMsg)
            }


            alertDialogBuilder.setPositiveButton("OK") { _, _ ->

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
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
    }
}