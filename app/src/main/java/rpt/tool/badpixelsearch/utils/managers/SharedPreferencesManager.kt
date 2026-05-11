package rpt.tool.badpixelsearch.utils.managers

import android.content.Context
import android.content.SharedPreferences
import rpt.tool.badpixelsearch.Application
import rpt.tool.badpixelsearch.utils.AppUtils
import androidx.core.content.edit


object SharedPreferencesManager {
    private val ctx: Context
        get() = Application.instance

    private fun createSharedPreferences(): SharedPreferences {
        return ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    private val sharedPreferences by lazy { createSharedPreferences() }

    var firstRun: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIRST_RUN_KEY, true)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIRST_RUN_KEY, value) }
    var mode: Int
        get() = sharedPreferences.getInt(AppUtils.MODE_KEY, 0)
        set(value) = sharedPreferences.edit() { putInt(AppUtils.MODE_KEY, value) }
    var velocity: Int
        get() = sharedPreferences.getInt(AppUtils.VELOCITY_KEY, 0)
        set(value) = sharedPreferences.edit() { putInt(AppUtils.VELOCITY_KEY, value) }
    var interval: Int
        get() = sharedPreferences.getInt(AppUtils.INTERVAL_KEY, 300000)
        set(value) = sharedPreferences.edit() { putInt(AppUtils.INTERVAL_KEY, value) }
    var fullBrightness: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FULL_BRIGHTNESS_KEY, true)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FULL_BRIGHTNESS_KEY, value) }
    var delay: Int
        get() = sharedPreferences.getInt(AppUtils.DELAY_KEY, 100)
        set(value) = sharedPreferences.edit() { putInt(AppUtils.DELAY_KEY, value) }
    var action: String?
        get() = sharedPreferences.getString(AppUtils.ACTION_KEY, "fix")
        set(value) = sharedPreferences.edit() { putString(AppUtils.ACTION_KEY, value) }
    var typeMode: Int
        get() = sharedPreferences.getInt(AppUtils.TYPE_MODE_KEY, 0)
        set(value) = sharedPreferences.edit() { putInt(AppUtils.TYPE_MODE_KEY, value) }
    var isVertical: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_VERTICAL, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.IS_VERTICAL, value) }
    var typeNoiseColored: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.NOISE_COLORED, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.NOISE_COLORED, value) }
    var IsBold:  Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_BOLD, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.IS_BOLD, value) }
    var IsBoldItalic:  Boolean
        get() = sharedPreferences.getBoolean(AppUtils.IS_BOLD_ITALIC, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.IS_BOLD_ITALIC, value) }
    var rgbOption : Int
        get() = sharedPreferences.getInt(AppUtils.RGB_OPTION, 0)
        set(value) = sharedPreferences.edit() { putInt(AppUtils.RGB_OPTION, value) }

    var sound:  Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SOUND_ON, true)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.SOUND_ON, value) }

    var colorTestPurity: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.COLOR_TEST_PURITY, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.COLOR_TEST_PURITY, value) }

    var colorTestGradient: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.COLOR_TEST_GRADIENT, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.COLOR_TEST_GRADIENT, value) }

    var colorTestScales: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.COLOR_TEST_SCALES, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.COLOR_TEST_SCALES, value) }

    var colorTestShades: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.COLOR_TEST_SHADES, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.COLOR_TEST_SHADES, value) }

    var colorTestGamma: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.COLOR_TEST_GAMMA, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.COLOR_TEST_GAMMA, value) }

    var colorTestLine: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.COLOR_TEST_LINE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.COLOR_TEST_LINE, value) }

    var animTest2D: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.ANIM_TEST_2D, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.ANIM_TEST_2D, value) }

    var animTest3D: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.ANIM_TEST_3D, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.ANIM_TEST_3D, value) }

    var animTest2DGravity: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.ANIM_TEST_2D_GRAVITY, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.ANIM_TEST_2D_GRAVITY, value) }

    var animTest3DGravity: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.ANIM_TEST_3D_GRAVITY, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.ANIM_TEST_3D_GRAVITY, value) }

    var animTestMovingBars: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.ANIM_TEST_MOVING_BARS, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.ANIM_TEST_MOVING_BARS, value) }

    var animTestRotation: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.ANIM_TEST_ROTATION, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.ANIM_TEST_ROTATION, value) }

    var cameraTest1: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.CAMERA_TEST_1, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.CAMERA_TEST_1, value) }

    var cameraTest2: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.CAMERA_TEST_2, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.CAMERA_TEST_2, value) }

    var fontTestNormal: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FONT_TEST_NORMAL, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FONT_TEST_NORMAL, value) }

    var fontTestItalic: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FONT_TEST_ITALIC, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FONT_TEST_ITALIC, value) }

    var fontTestFamilies: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FONT_TEST_FAMILIES, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FONT_TEST_FAMILIES, value) }

    var fontTestReading: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FONT_TEST_READING, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FONT_TEST_READING, value) }

    var rgbTestRed: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.RGB_TEST_RED, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.RGB_TEST_RED, value) }

    var rgbTestGreen: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.RGB_TEST_GREEN, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.RGB_TEST_GREEN, value) }

    var rgbTestBlue: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.RGB_TEST_BLUE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.RGB_TEST_BLUE, value) }

    var rgbTestGray: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.RGB_TEST_GRAY, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.RGB_TEST_GRAY, value) }

    var rgbTestAll: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.RGB_TEST_ALL, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.RGB_TEST_ALL, value) }

    var rgbTestMixer: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.RGB_TEST_MIXER, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.RGB_TEST_MIXER, value) }

    var fixTestBW: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_BW, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_BW, value) }

    var fixTestNoise: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_NOISE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_NOISE, value) }

    var fixTestSnow: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_SNOW, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_SNOW, value) }

    var fixTestHorLine: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_HOR_LINE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_HOR_LINE, value) }

    var fixTestVerLine: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_VER_LINE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_VER_LINE, value) }

    var fixTestHorRect: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_HOR_RECT, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_HOR_RECT, value) }

    var fixTestVerRect: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_VER_RECT, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_VER_RECT, value) }

    var fixTestFix: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIX_TEST_FIX, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.FIX_TEST_FIX, value) }

    var singleTouch: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.TOUCH_TEST_SINGLE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.TOUCH_TEST_SINGLE, value) }

    var singleTouchTwo: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.TOUCH_TWO_TEST_SINGLE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.TOUCH_TWO_TEST_SINGLE, value) }

    var multiTouch: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.TOUCH_TEST_MULTI, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.TOUCH_TEST_MULTI, value) }

    var multiTouchTwo: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.TOUCH_TWO_TEST_MULTI, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.TOUCH_TWO_TEST_MULTI, value) }

    var zoom: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.TOUCH_TEST_ZOOM, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.TOUCH_TEST_ZOOM, value) }

    var responseTime: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.TOUCH_TEST_RESPONSE, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.TOUCH_TEST_RESPONSE, value) }

    var normalLines: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DRAWING_NORMAL_TEST, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.DRAWING_NORMAL_TEST, value) }

    var normalLinesTwo: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DRAWING_NORMAL_TWO_TEST, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.DRAWING_NORMAL_TWO_TEST, value) }

    var fadingLines: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DRAWING_FADING_TEST, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.DRAWING_FADING_TEST, value) }

    var fadingLinesTwo: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DRAWING_FADING_TWO_TEST, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.DRAWING_FADING_TWO_TEST, value) }

    var stylusLines: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DRAWING_STYLUS_TEST, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.DRAWING_STYLUS_TEST, value) }

    var colorLines: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.DRAWING_COLOR_TEST, false)
        set(value) = sharedPreferences.edit() { putBoolean(AppUtils.DRAWING_COLOR_TEST, value) }

}