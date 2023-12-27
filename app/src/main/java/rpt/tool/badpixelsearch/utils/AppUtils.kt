package rpt.tool.badpixelsearch.utils

import android.view.Window
import android.view.WindowManager





class AppUtils {
    companion object {

        fun setFullBrightness(window: Window?, fullBrightness: Boolean) {
            val lp = window!!.attributes
            lp.screenBrightness =
                if (fullBrightness) WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
                else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            window!!.attributes = lp
        }


        const val USERS_SHARED_PREF = "user_pref"
        val PRIVATE_MODE = 0
        const val FIRST_RUN_KEY = "firstrun"

    }
}