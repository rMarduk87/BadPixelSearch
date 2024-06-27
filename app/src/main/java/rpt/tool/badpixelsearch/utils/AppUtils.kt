package rpt.tool.badpixelsearch.utils

import rpt.tool.badpixelsearch.utils.extensions.roundToString


class AppUtils {
    companion object {
        fun getAspectRatio(w: Int, h: Int): String {
            return if( h/w >= (4/3)  ) { // 4:3
                "4:3"
            } else if( h/w >= (16/9)  ) { // 16:9
                "16:9"
            } else if( h/w >= (16/10) ) { // 16:10
                "16:10"
            } else if(h/w >= (21/9) ) { // 21:9
                "21:9"
            } else if(h/w >= (3/2) ) { // 3:2
                "3:2"
            }else {
                "strings"
            }
        }

        fun getScreenBrightness(brightness: Float): String {
            return brightness.roundToString() + " (" + (brightness*100/250).roundToString() + " %, "
        }


        const val USERS_SHARED_PREF : String = "user_pref"
        val PRIVATE_MODE = 0
        const val FIRST_RUN_KEY : String = "firstrun"
        const val DELAY_KEY : String = "delay"
        const val ACTION_KEY : String = "action"
        const val MODE_KEY: String  = "scrolling_mode"
        const val VELOCITY_KEY: String = "scrolling_speed"
        const val INTERVAL_KEY: String = "interval"
        const val FULL_BRIGHTNESS_KEY: String = "full_brightness"
        const val one = 60000
        const val five = 300000
        const val fifthy = 900000
        const val thirty = 1800000
        const val hour = 3600000


    }
}