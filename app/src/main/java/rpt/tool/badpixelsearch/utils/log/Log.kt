package rpt.tool.badpixelsearch.utils.log

import timber.log.Timber

internal class Log {
    companion object {
        private const val LOGPREFIX = "RPTLog-"

        internal fun logDebug(message: String?, className: String) {
            Timber.tag("$LOGPREFIX$className").d(message)
        }

        internal fun logWarning(message: String?, className: String) {
            Timber.tag("$LOGPREFIX$className").w(message)
        }

        internal fun logThrowable(throwable: Throwable, className: String, message: String? = null) {
            Timber.tag("$LOGPREFIX$className").e(throwable, message)
            throwable.printStackTrace()
        }
    }
}