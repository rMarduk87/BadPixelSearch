package rpt.tool.badpixelsearch.utils.log

import timber.log.Timber

internal class Log {
    companion object {
        private const val logPrefix = "RPTLog-"

        internal fun logDebug(message: String?, className: String) {
            Timber.tag("$logPrefix$className").d(message)
        }

        internal fun logWarning(message: String?, className: String) {
            Timber.tag("$logPrefix$className").w(message)
        }

        internal fun logThrowable(throwable: Throwable, className: String, message: String? = null) {
            Timber.tag("$logPrefix$className").e(throwable, message)
            throwable.printStackTrace()
        }

        internal fun logInfo(message: String?, className: String) {
            Timber.tag("$logPrefix$className").i(message)
        }

        internal fun logVerbose(message: String?, className: String) {
            Timber.tag("$logPrefix$className").v(message)
        }
    }
}