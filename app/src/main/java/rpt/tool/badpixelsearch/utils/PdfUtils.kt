package rpt.tool.badpixelsearch.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object PdfUtils {

    fun shareInfoAsPdf(context: Context, title: String, infoList: List<Pair<String, String>>) {
        val pdfDocument = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas
        val paint = Paint()

        var y = 50f
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText(title, 50f, y, paint)
        
        y += 40f
        paint.textSize = 12f
        paint.isFakeBoldText = false

        for (info in infoList) {
            if (y > pageHeight - 50) {
                pdfDocument.finishPage(page)
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 50f
            }
            canvas.drawText("${info.first}: ${info.second}", 50f, y, paint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        val fileName = "${title.replace(" ", "_")}.pdf"
        val file = File(context.cacheDir, fileName)
        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        pdfDocument.close()

        shareFile(context, file)
    }

    private fun shareFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(intent, "Share PDF"))
    }
}