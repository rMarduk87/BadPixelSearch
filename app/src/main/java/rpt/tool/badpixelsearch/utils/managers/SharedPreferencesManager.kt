package rpt.tool.badpixelsearch.utils.managers

import android.content.Context
import android.content.SharedPreferences
import rpt.tool.badpixelsearch.Application
import rpt.tool.badpixelsearch.utils.AppUtils


object SharedPreferencesManager {
    private val ctx: Context
        get() = Application.instance

    private fun createSharedPreferences(): SharedPreferences {
        return ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    private val sharedPreferences by lazy { createSharedPreferences() }

    var firstRun: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIRST_RUN_KEY, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.FIRST_RUN_KEY, value).apply()
    var mode: Int
        get() = sharedPreferences.getInt(AppUtils.MODE_KEY, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.MODE_KEY, value).apply()
    var velocity: Int
        get() = sharedPreferences.getInt(AppUtils.VELOCITY_KEY, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.VELOCITY_KEY, value).apply()
}