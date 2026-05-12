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
        const val SOUND_ON: String = "sound_on"

        const val COLOR_TEST_PURITY: String = "color_test_purity"
        const val COLOR_TEST_GRADIENT: String = "color_test_gradient"
        const val COLOR_TEST_SCALES: String = "color_test_scales"
        const val COLOR_TEST_SHADES: String = "color_test_shades"
        const val COLOR_TEST_GAMMA: String = "color_test_gamma"
        const val COLOR_TEST_LINE: String = "color_test_line"

        const val ANIM_TEST_2D: String = "anim_test_2d"
        const val ANIM_TEST_3D: String = "anim_test_3d"
        const val ANIM_TEST_2D_GRAVITY: String = "anim_test_2d_gravity"
        const val ANIM_TEST_3D_GRAVITY: String = "anim_test_3d_gravity"
        const val ANIM_TEST_MOVING_BARS: String = "anim_test_moving_bars"
        const val ANIM_TEST_ROTATION: String = "anim_test_rotation"

        const val CAMERA_TEST_1: String = "camera_test_1"
        const val CAMERA_TEST_2: String = "camera_test_2"

        const val FONT_TEST_NORMAL: String = "font_test_normal"
        const val FONT_TEST_ITALIC: String = "font_test_italic"
        const val FONT_TEST_FAMILIES: String = "font_test_families"
        const val FONT_TEST_READING: String = "font_test_reading"

        const val RGB_TEST_RED: String = "rgb_test_red"
        const val RGB_TEST_GREEN: String = "rgb_test_green"
        const val RGB_TEST_BLUE: String = "rgb_test_blue"
        const val RGB_TEST_GRAY: String = "rgb_test_gray"
        const val RGB_TEST_ALL: String = "rgb_test_all"
        const val RGB_TEST_MIXER: String = "rgb_test_mixer"

        const val FIX_TEST_BW: String = "fix_test_bw"
        const val FIX_TEST_NOISE: String = "fix_test_noise"
        const val FIX_TEST_SNOW: String = "fix_test_snow"
        const val FIX_TEST_HOR_LINE: String = "fix_test_hor_line"
        const val FIX_TEST_VER_LINE: String = "fix_test_ver_line"
        const val FIX_TEST_HOR_RECT: String = "fix_test_hor_rect"
        const val FIX_TEST_VER_RECT: String = "fix_test_ver_rect"
        const val TOUCH_TEST_SINGLE: String = "touch_test_single"
        const val TOUCH_TWO_TEST_SINGLE: String = "touch_two_test_single"
        const val TOUCH_TEST_MULTI: String = "touch_test_multi"
        const val TOUCH_TWO_TEST_MULTI: String = "touch_two_test_multi"
        const val TOUCH_TEST_ZOOM: String = "touch_test_zoom"
        const val TOUCH_TEST_RESPONSE: String = "touch_test_response"
        const val DRAWING_NORMAL_TEST: String = "drawing_normal_test"
        const val DRAWING_NORMAL_TWO_TEST: String = "drawing_normal_two_test"
        const val DRAWING_FADING_TEST: String = "drawing_fading_test"
        const val DRAWING_FADING_TWO_TEST: String = "drawing_fading_two_test"
        const val DRAWING_STYLUS_TEST: String = "drawing_stylus_test"
        const val DRAWING_COLOR_TEST: String = "drawing_color_test"
        const val FIX_TEST_FIX: String = "fix_test_fix"
        const val DRAWING_OPTION: String = "drawing_option"
    }
}