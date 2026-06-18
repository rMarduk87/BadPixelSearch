package rpt.tool.badpixelsearch

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import rpt.com.base.Inflate
import rpt.com.base.log.e
import rpt.com.base.navigation.safeNavController
import rpt.tool.badpixelsearch.utils.managers.SharedPreferencesManager
import rpt.com.base.BaseFragment as CoreBaseFragment

abstract class BaseFragment<VB : ViewBinding>(
    inflate: Inflate<VB>,
    hideBars: Boolean = false
) : CoreBaseFragment<VB>(inflate, hideBars) {


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
                    val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.goodbye)
                    mediaPlayer?.setOnCompletionListener { it.release() }
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
                e(Throwable(e), "Sound")
            }
            safeNavController(R.id.main_activity_nav_host_fragment)?.popBackStack()
        }
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    protected fun checkAndAdjustToolbar(toolbar: View) {
        val updatePosition = { insets: WindowInsetsCompat? ->
            val activity = activity
            if (activity != null) {
                val usedInsets = insets ?: ViewCompat.getRootWindowInsets(activity.window.decorView)

                val cutoutTop = usedInsets?.displayCutout?.safeInsetTop ?: 0
                val systemBarsTop = usedInsets?.getInsetsIgnoringVisibility(
                    WindowInsetsCompat.Type.systemBars())?.top ?: 0

                var offset = maxOf(cutoutTop, systemBarsTop)

                if (offset <= 0) {
                    val resourceId =
                        resources
                            .getIdentifier("status_bar_height",
                                "dimen", "android")
                    if (resourceId > 0) {
                        offset = resources.getDimensionPixelSize(resourceId)
                    }
                }

                if (offset > 0) {
                    // Apply padding to the fragment's root content layout.
                    val root = binding.root
                    val targetView = if (root is androidx.drawerlayout.widget.DrawerLayout) {
                        root.getChildAt(0)
                    } else {
                        root
                    }

                    if (targetView != null && targetView.paddingTop != offset) {
                        targetView.setPadding(targetView.paddingLeft, offset,
                            targetView.paddingRight, targetView.paddingBottom)
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