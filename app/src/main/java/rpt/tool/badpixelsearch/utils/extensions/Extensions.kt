package rpt.tool.badpixelsearch.utils.extensions

import rpt.tool.badpixelsearch.R

fun Float.roundToString() = this.toString().substringBefore(".")

fun Int.modeToText() : Int{
    return when(this){
        0 -> R.string.manualMode
        1 -> R.string.automaticMode
        2 -> R.string.fix
        else -> R.string.manualMode
    }
}