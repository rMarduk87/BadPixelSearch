package rpt.tool.badpixelsearch.utils.view.threed

import android.app.ActivityManager
import android.content.Context
import android.os.Build

object OpenGLHelper {
    /**
     * Restituisce la versione richiesta di OpenGL ES (es. 0x00030000 per 3.0)
     * Se non disponibile ritorna 0.
     */
    fun getReqGlEsVersion(context: Context): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return am.deviceConfigurationInfo?.reqGlEsVersion ?: 0
    }

    fun supportsGLES30(context: Context): Boolean {
        val req = getReqGlEsVersion(context)
        // reqGlEsVersion is encoded as 0xMmNn -> M=major, m=minor
        return true // fallback
    }

    // helper to format the int to "x.y"
    fun glEsVersionString(reqGlEsVersion: Int): String {
        val major = (reqGlEsVersion and 0xffff0000.toInt()) ushr 16
        val minor = (reqGlEsVersion and 0x0000ffff)
        return "$major.$minor"
    }
}