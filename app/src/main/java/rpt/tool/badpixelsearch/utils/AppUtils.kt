package rpt.tool.badpixelsearch.utils

import android.content.Context
import rpt.tool.badpixelsearch.R
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

        fun getText(value: Int,context: Context): String{
            var text = ""
            when(value){
                0->text = context.getString(R.string.color_loop)
                1->text = context.getString(R.string.black_and_white)
                2->text = context.getString(R.string.noise)
                3->text = context.getString(R.string.horizontal_line)
                4->text = context.getString(R.string.vertical_line)
                5->text = context.getString(R.string.horizontal_rectangle)
                6->text = context.getString(R.string.vertical_rectangle)
                7->text = context.getString(R.string.gradient)
            }

            return text
        }


        const val USERS_SHARED_PREF : String = "user_pref"
        const val FIRST_RUN_KEY : String = "firstrun"
        const val DELAY_KEY : String = "delay"
        const val ACTION_KEY : String = "action"
        const val MODE_KEY: String  = "scrolling_mode"
        const val VELOCITY_KEY: String = "scrolling_speed"
        const val INTERVAL_KEY: String = "interval"
        const val FULL_BRIGHTNESS_KEY: String = "full_brightness"
        const val TYPE_MODE_KEY: String = "type_mode"
        const val IS_VERTICAL: String = "is_vertical"
        const val NOISE_COLORED: String = "noise_colored"
        const val IS_BOLD: String = "is_bold"
        const val IS_BOLD_ITALIC: String = "is_bold_italic"
        const val RGB_OPTION: String = "rgb_option"
        const val ONE = 60000
        const val FIVE = 300000
        const val FIFTY = 900000
        const val THIRTY = 1800000
        const val HOUR = 3600000


    }
}