package rpt.tool.badpixelsearch.ui.menu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentMenuBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.navigation.safeNavigate
import androidx.core.net.toUri
import rpt.tool.badpixelsearch.BadPixelSearchActivity
import rpt.tool.badpixelsearch.PixelTestActivity
import rpt.tool.badpixelsearch.NoiseSearchActivity
import rpt.tool.badpixelsearch.GradientTestActivity


@Suppress("DEPRECATION")
class MenuFragment :
    BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate){

    private val durationValue = 6000L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedPreferencesManager.firstRun = false

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
                MenuFragmentDirections.actionMenuFragmentToSettingsFragment())
        }

        binding.openFaqMenuBtn.setOnClickListener{
            safeNavController?.safeNavigate(
                MenuFragmentDirections.actionMenuFragmentToFaqFragment())
        }

        binding.screenInfoBtn.setOnClickListener{
            safeNavController?.safeNavigate(
                MenuFragmentDirections.actionMenuFragmentToScreenInfoFragment())
        }

        binding.strBtn.setOnClickListener{
            when(SharedPreferencesManager.typeMode){
                0,1->startActivity(Intent(requireContext(),BadPixelSearchActivity::class.java))
                2->startActivity(Intent(requireContext(),NoiseSearchActivity::class.java))
                3,4,5,6->startActivity(Intent(requireContext(),PixelTestActivity::class.java))
                7->startActivity(Intent(requireContext(),GradientTestActivity::class.java))
            }
        }

        binding.deviceInfoBtn.setOnClickListener{
            safeNavController?.safeNavigate(
                MenuFragmentDirections.actionMenuFragmentToDeviceInfoFragment())
        }

        binding.otherAppBtn.setOnClickListener{
            safeNavController?.safeNavigate(
                MenuFragmentDirections.actionMenuFragmentToOtherAppsFragment())
        }

        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        val width = binding.logoAnimated.measuredWidth.toFloat()

        val animator1 = ObjectAnimator
            .ofFloat(binding.logoAnimated,
                "translationX", 0f, -(width - point.x)).apply {
                    duration = durationValue
                    repeatCount = 1
                    repeatMode = ValueAnimator.REVERSE
            }

        val animator2 = ObjectAnimator
            .ofFloat(binding.logoAnimated,"translationX",
                0f, +(width - point.x)).apply {
                duration = durationValue
                repeatCount = 1
                repeatMode = ValueAnimator.REVERSE
            }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2)
        animatorSet.start()
    }

    @SuppressLint("IntentReset")
    private fun sendMail(
        activity: Activity,
        emailIds: Array<String>,
        subject: String,
        textMessage: String
    ) {

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.setData("mailto:".toUri())
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
}