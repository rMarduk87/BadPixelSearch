package rpt.tool.badpixelsearch.utils.managers

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
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

}