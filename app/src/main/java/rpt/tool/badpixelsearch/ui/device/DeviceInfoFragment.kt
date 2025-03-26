package rpt.tool.badpixelsearch.ui.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import com.torrydo.screenez.ScreenEz
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.MainActivity
import rpt.tool.badpixelsearch.databinding.FragmentDeviceInfoBinding


class DeviceInfoFragment : BaseFragment<FragmentDeviceInfoBinding>(FragmentDeviceInfoBinding::inflate) {

    private var model = ""
    private var manufacturer = ""
    private var product = ""
    private var device = ""
    private var brand = ""
    private var board = ""
    private var hardware = ""
    private var architecture = ""
    private var cpuABI = ""
    private var deviceID = ""
    private var display = ""
    private var android = ""
    private var sdk = ""


    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leftIconBlock.setOnClickListener{ finish() }

        model = Build.MODEL
        manufacturer = Build.MANUFACTURER
        product = Build.PRODUCT
        device = Build.DEVICE
        brand = Build.BRAND
        board = Build.BOARD
        hardware = Build.HARDWARE
        architecture = System.getProperty("os.arch")?.toString().toString() +","+Build.CPU_ABI2
        cpuABI = Build.CPU_ABI2+","+Build.CPU_ABI2
        deviceID = Settings.Secure.getString(requireContext().contentResolver,
            Settings.Secure.ANDROID_ID)
        this.display = Build.DISPLAY
        android = ""+Build.VERSION.RELEASE
        sdk = ""+Build.VERSION.SDK_INT

        binding.textView2.text = model
        binding.textView22.text = manufacturer
        binding.textView32.text = product
        binding.textView42.text = device
        binding.textView52.text = brand
        binding.textView62.text = board
        binding.textView72.text = hardware
        binding.textView82.text = architecture
        binding.textView92.text = cpuABI
        binding.textView102.text = deviceID
        binding.textView112.text = this.display
        binding.textView122.text = android
        binding.textView132.text = sdk
    }

    private fun finish() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }
}