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

fun String.toEscapedString() : String{
    var spaceCount = 0
    for (c in this.toCharArray()) {
        if (c == ' ') {
            spaceCount++
        }
    }
    if(spaceCount==0){
        return this
    }
    return this.replace(' ','\n',true)
}

fun String.toNewLineString() : String{
    var spaceCount = 0
    for (c in this.toCharArray()) {
        if (c == ' ') {
            spaceCount++
        }
    }
    if(spaceCount==0){
        return this
    }

    val start: Int = this.lastIndexOf(' ')
    val builder = StringBuilder()
    builder.append(this.substring(0, start))
    builder.append("\n").append("px")


    return builder.toString()
}