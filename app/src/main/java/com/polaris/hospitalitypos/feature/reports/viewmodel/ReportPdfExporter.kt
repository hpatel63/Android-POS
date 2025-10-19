package com.polaris.hospitalitypos.feature.reports.viewmodel

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReportPdfExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun export(report: ReportItem) = withContext(Dispatchers.IO) {
        val document = PrintedPdfDocument(context, PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.NA_LETTER).build())
        val page = document.startPage(1)
        val canvas = page.canvas
        canvas.drawText(report.title, 72f, 72f, android.graphics.Paint().apply { textSize = 24f })
        canvas.drawText(report.description, 72f, 120f, android.graphics.Paint().apply { textSize = 16f })
        document.finishPage(page)
        val outputDir = File(context.filesDir, "reports").apply { mkdirs() }
        val outputFile = File(outputDir, "${report.title.replace(' ', '_')}.pdf")
        FileOutputStream(outputFile).use { document.writeTo(it) }
        document.close()
        outputFile
    }
}
