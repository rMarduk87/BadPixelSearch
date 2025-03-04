package rpt.tool.badpixelsearch.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.View
import android.view.WindowManager
import com.torrydo.screenez.ScreenEz
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.MainActivity
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.FragmentScreenInfoBinding
import rpt.tool.badpixelsearch.utils.AppUtils
import rpt.tool.badpixelsearch.utils.extensions.roundToString
import rpt.tool.badpixelsearch.utils.extensions.toEscapedString
import rpt.tool.badpixelsearch.utils.extensions.toNewLineString
import kotlin.math.sqrt


@Suppress("DEPRECATION")
class ScreenInfoFragment :
    BaseFragment<FragmentScreenInfoBinding>(FragmentScreenInfoBinding::inflate) {

    private var fullResolution = ""
    private var currentResolution = ""
    private var visualResolution = ""
    private var screenScale = ""
    private var pixelDensity = ""
    private var screenSize = ""
    private var refreshRate = ""
    private var screenType = ""
    private var aspectRatio = ""
    private var screenBrightness = ""
    private var wideColorGamut = ""
    private var hdrScreen = ""
    private var pixelFormat = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ScreenEz.with(requireContext())

        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        binding.leftIconBlock.setOnClickListener{ finish() }

        fullResolution = getScreenResolution().toNewLineString()
        currentResolution = getScreenResolution().toNewLineString()
        visualResolution = getVisualResolution().toNewLineString()
        screenScale = getScreenScale()
        pixelDensity = getPixelDensity()
        screenSize = getScreenSize()
        refreshRate = display.refreshRate.roundToString() + " Hz"
        screenType = display.name.toEscapedString()
        aspectRatio = getAspectRatio()
        screenBrightness = getScreenBrightness()
        wideColorGamut = if(display.isWideColorGamut) requireContext().getString(R.string.yes)
        else requireContext().getString(R.string.no)
        hdrScreen = if(display.isHdr) requireContext().getString(R.string.yes)
        else requireContext().getString(R.string.no)
        pixelFormat = "RGBA_8888"

        binding.textView2.text = fullResolution
        binding.textView22.text = currentResolution
        binding.textView32.text = visualResolution
        binding.textView42.text = screenScale
        binding.textView52.text = pixelDensity
        binding.textView62.text = screenSize
        binding.textView72.text = refreshRate
        binding.textView82.text = screenType
        binding.textView92.text = aspectRatio
        binding.textView102.text = screenBrightness
        binding.textView112.text = wideColorGamut
        binding.textView122.text = hdrScreen
        binding.textView132.text = pixelFormat
    }

    private fun finish() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun getScreenResolution(): String {
        val w = ScreenEz.fullWidth
        val h = ScreenEz.fullHeight
        return "$w X $h px"
    }

    private fun getVisualResolution(): String {
        val w = ScreenEz.safeWidth
        val h = ScreenEz.safeHeight
        return "$w X $h px"
    }

    private fun getScreenScale(): String {
        val metrics = requireContext().resources.displayMetrics
        val ratio = (metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat())
        return "%.2f".format(ratio)
    }

    private fun getPixelDensity(): String {
        val metrics = resources.displayMetrics
        val densityDpi = (metrics.density * 160f).toInt()
        return "$densityDpi dpi"
    }

    private fun getScreenSize(): String {
        try {
            val displayMetrics = requireActivity().resources.displayMetrics

            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalInches = "%.2f".format(sqrt((xInches * xInches + yInches * yInches).toDouble()))
            return "$diagonalInches inches"
        } catch (e: Exception) {
            return "-1"
        }
    }

    private fun getAspectRatio(): String {
        val w = ScreenEz.fullWidth
        val h = ScreenEz.fullHeight
        val aspect = AppUtils.getAspectRatio(w,h)
        return if(aspect!="strings") aspect else requireContext().getString(R.string.unknown)
    }

    private fun getScreenBrightness(): String {
        var brightness = 0f
        var mode = 0
        try {
            brightness = Settings.System.getInt(requireContext().contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
            ).toFloat()
            mode = Settings.System.getInt(requireContext().contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE
            )
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }

        return AppUtils.getScreenBrightness(brightness)+"\n" + (if(mode==0)
            requireContext().getString(R.string.manualMode) else
                requireContext().getString(R.string.automaticMode)) + ")"
    }




}