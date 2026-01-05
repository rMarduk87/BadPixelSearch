package rpt.tool.badpixelsearch.ui.camera.menu

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import rpt.tool.badpixelsearch.BaseFragment
import rpt.tool.badpixelsearch.databinding.FragmentMenuCameraBinding

class CameraMenuFragment :
    BaseFragment<FragmentMenuCameraBinding>(FragmentMenuCameraBinding::inflate) {

    private var canClick = true
    private val durationValue = 6000L

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                canClick = false
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Chiedi permessi solo all'apertura
        permissionLauncher.launch(Manifest.permission.CAMERA)

        binding.openFrontCameraTest.setOnClickListener {
            if(canClick){
                showPreview()
                startCamera(CameraSelector.DEFAULT_FRONT_CAMERA)

            }
        }

        binding.openRearCameraTest.setOnClickListener {
            if(canClick){
                showPreview()
                startCamera(CameraSelector.DEFAULT_BACK_CAMERA)
            }
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

    private fun showPreview() {
        if (binding.previewCamera.visibility != View.VISIBLE) {
            binding.previewCamera.visibility = View.VISIBLE
        }
    }

    private fun startCamera(selector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            preview.setSurfaceProvider(binding.previewCamera.surfaceProvider)

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, selector,
                preview)

        }, ContextCompat.getMainExecutor(requireContext()))
    }
}