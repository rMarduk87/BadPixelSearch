package rpt.tool.badpixelsearch

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import rpt.tool.badpixelsearch.utils.Inflate
import rpt.tool.badpixelsearch.utils.log.e
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.tool.badpixelsearch.utils.navigation.safeNavController


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun setupToolbar(btnBack: View, menuTitle: TextView? = null, title: String? = null) {
        title?.let { menuTitle?.text = it }
        
        // Ensure the window allows drawing in the cutout area
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity?.window?.attributes?.let { attrs ->
                if (attrs.layoutInDisplayCutoutMode != WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES) {
                    attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    activity?.window?.attributes = attrs
                }
            }
        }

        val toolbar = btnBack.parent as? View
        toolbar?.let { checkAndAdjustToolbar(it) }

        btnBack.setOnClickListener {
            try {
                if (SharedPreferencesManager.sound) {
                    val mediaPlayer = MediaPlayer.create(requireContext(),
                        R.raw.goodbye)
                    mediaPlayer?.setOnCompletionListener { it.release() }
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
                e(Throwable(e), "Sound")
            }
            safeNavController?.popBackStack()
        }
    }

    protected fun checkAndAdjustToolbar(toolbar: View) {
        val updatePosition = { insets: WindowInsetsCompat? ->
            val activity = activity
            if (activity != null) {
                val usedInsets = insets ?: ViewCompat.getRootWindowInsets(activity.window.decorView)
                
                val cutoutTop = usedInsets?.displayCutout?.safeInsetTop ?: 0
                val systemBarsTop = usedInsets?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())?.top ?: 0
                
                var offset = maxOf(cutoutTop, systemBarsTop)
                
                if (offset <= 0) {
                    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                    if (resourceId > 0) {
                        offset = resources.getDimensionPixelSize(resourceId)
                    }
                }

                if (offset > 0) {
                    // Apply padding to the fragment's root content layout.
                    // This moves EVERYTHING (toolbar and all other views) down together.
                    val root = binding.root
                    val targetView = if (root is androidx.drawerlayout.widget.DrawerLayout) {
                        root.getChildAt(0)
                    } else {
                        root
                    }

                    if (targetView != null && targetView.paddingTop != offset) {
                        targetView.setPadding(targetView.paddingLeft, offset, targetView.paddingRight, targetView.paddingBottom)
                    }

                    // Reset any direct modifications to the toolbar view itself
                    toolbar.translationY = 0f
                    val tParams = toolbar.layoutParams as? ViewGroup.MarginLayoutParams
                    if (tParams != null && tParams.topMargin != 0) {
                        tParams.topMargin = 0
                        toolbar.layoutParams = tParams
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { _, insets ->
            updatePosition(insets)
            insets
        }

        toolbar.post { updatePosition(null) }
    }

}
