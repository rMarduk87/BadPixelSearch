package rpt.tool.badpixelsearch.ui.camera.menu

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import android.widget.LinearLayout
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.R
import rpt.tool.badpixelsearch.databinding.TestsMenuTwoBinding
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import android.media.MediaPlayer

class CameraMenuFragment :
    BaseFragment<TestsMenuTwoBinding>(TestsMenuTwoBinding::inflate) {

    private var canClick = true
    private val PULSE_DURATION = 2500L
    private var previewCamera: PreviewView? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                canClick = false
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            binding.toolbar.btnBack,
            binding.toolbar.menuTitle,
            getString(R.string.camera_tests)
        )

        binding.iconAnimated.setImageResource(R.drawable.ic_camera)

        binding.text1.text = requireContext().getString(R.string.camera1)
        binding.text2.text = requireContext().getString(R.string.camera2)

        setupDynamicCameraPreview()
        setupNumbers()

        ObjectAnimator.ofFloat(binding.iconAnimated, "alpha",
            1f, 0f).apply {
            duration = PULSE_DURATION
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        permissionLauncher.launch(Manifest.permission.CAMERA)

        binding.option1.setOnClickListener {
            executeWithSound {
                if(canClick){
                    binding.touch1.visibility = View.VISIBLE
                    SharedPreferencesManager.cameraTest1 = true
                    showPreview()
                    startCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
                }
            }
        }

        binding.option2.setOnClickListener {
            executeWithSound {
                if(canClick){
                    binding.touch2.visibility = View.VISIBLE
                    SharedPreferencesManager.cameraTest2 = true
                    showPreview()
                    startCamera(CameraSelector.DEFAULT_BACK_CAMERA)
                }
            }
        }
    }

    private fun setupDynamicCameraPreview() {
        val context = requireContext()
        previewCamera = PreviewView(context).apply {
            visibility = View.GONE
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (300 * context.resources.displayMetrics.density).toInt()
            ).apply {
                setMargins(0, 0, 0, (20 * context.resources.displayMetrics.density).toInt())
            }
        }
        val container = binding.iconAnimated.parent as? LinearLayout
        container?.addView(previewCamera, container.indexOfChild(binding.iconAnimated) + 1)
    }

    private fun setupNumbers() {
        if (SharedPreferencesManager.cameraTest1) {
            binding.touch1.visibility = View.VISIBLE
        }
        if (SharedPreferencesManager.cameraTest2) {
            binding.touch2.visibility = View.VISIBLE
        }
    }

    private fun showPreview() {
        if (previewCamera?.visibility != View.VISIBLE) {
            previewCamera?.visibility = View.VISIBLE
            binding.iconAnimated.visibility = View.GONE
        }
    }

    private fun startCamera(selector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            preview.surfaceProvider = previewCamera?.surfaceProvider

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, selector, preview)

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun executeWithSound(action: () -> Unit) {
        if (SharedPreferencesManager.sound) {
            try {
                val mediaPlayer = MediaPlayer.create(requireContext(),
                    R.raw.click_sound)
                mediaPlayer?.setOnCompletionListener { it.release() }
                mediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        action()
    }
}