package rpt.tool.badpixelsearch

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class Application  : Application() {

    companion object {

        private lateinit var _instance: Application

        val instance: Application
            get() {
                return _instance
            }
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }
}