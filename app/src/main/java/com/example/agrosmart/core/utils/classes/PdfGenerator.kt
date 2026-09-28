package com.example.agrosmart.core.utils.classes

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import com.example.agrosmart.domain.models.DiagnosisHistory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.withTranslation

object PdfGenerator {
    private const val PAGE_WIDTH = 595 // Ancho A4 en puntos
    private const val PAGE_HEIGHT = 842 // Alto A4 en puntos
    private const val MARGIN = 40

    fun generateDiagnosisHistoryPdf(
        context: Context,
        historyList: List<DiagnosisHistory>?
    ) {
        if (historyList.isNullOrEmpty()) {
            Toast.makeText(
                    context,
                    "No hay datos para generar el PDF.",
                    Toast.LENGTH_SHORT
            )
                .show()
            return
        }

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(
                PAGE_WIDTH,
                PAGE_HEIGHT,
                1
        )
            .create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas

        val titlePaint = TextPaint()
        titlePaint.color = Color.BLACK
        titlePaint.textSize = 18f
        titlePaint.typeface = Typeface.create(
                Typeface.DEFAULT,
                Typeface.BOLD
        )

        val headerPaint = TextPaint()
        headerPaint.color = Color.BLACK
        headerPaint.textSize = 12f
        headerPaint.typeface = Typeface.create(
                Typeface.DEFAULT,
                Typeface.BOLD
        )

        val bodyPaint = TextPaint()
        bodyPaint.color = Color.DKGRAY
        bodyPaint.textSize = 11f

        val linePaint = Paint()
        linePaint.color = Color.LTGRAY
        linePaint.strokeWidth = 1f

        var y = MARGIN

        // Título del documento
        canvas.drawText(
                "Historial de Diagnósticos Guardados",
                MARGIN.toFloat(),
                y.toFloat(),
                titlePaint
        )
        y += 40

        for (history in historyList) {
            // Si el contenido no cabe, crea una nueva página
            if (y > PAGE_HEIGHT - MARGIN * 2) { // Margen inferior
                document.finishPage(page)
                page = document.startPage(pageInfo)
                canvas = page.canvas
                y = MARGIN
            }

            // Dibujar línea separadora
            canvas.drawLine(
                    MARGIN.toFloat(),
                    y.toFloat(),
                    (PAGE_WIDTH - MARGIN).toFloat(),
                    y.toFloat(),
                    linePaint
            )
            y += 20

            // Contenido del diagnóstico
            canvas.drawText(
                    "Cultivo: " + history.crop!!.cropName,
                    MARGIN.toFloat(),
                    y.toFloat(),
                    headerPaint
            )
            y += 15
            canvas.drawText(
                    "Enfermedad: " + history.deficiency,
                    MARGIN.toFloat(),
                    y.toFloat(),
                    headerPaint
            )
            y += 15
            canvas.drawText(
                    "Fecha: " + history.diagnosisDate,
                    MARGIN.toFloat(),
                    y.toFloat(),
                    bodyPaint
            )
            y += 20

            // Para el texto de recomendación, usamos StaticLayout para manejar saltos de línea
            canvas.withTranslation(
                    MARGIN.toFloat(),
                    y.toFloat()
            ) {
                val recommendationLayout = StaticLayout(
                        "Recomendación: " + history.recommendation,
                        bodyPaint,
                        PAGE_WIDTH - MARGIN * 2,
                        Layout.Alignment.ALIGN_CENTER,
                        1.0f,
                        0.0f,
                        false
                )
                recommendationLayout.draw(this)
                y += recommendationLayout.height + 20 // Añadir espacio después del texto
            }
        }

        document.finishPage(page)

        // Guardar el archivo
        val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
        ).format(Date())
        val fileName = "AgroSmart_Diagnosticos_$timeStamp.pdf"
        val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                fileName
        )

        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(
                    context,
                    "PDF guardado en: " + file.absolutePath,
                    Toast.LENGTH_LONG
            )
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                    context,
                    "Error al guardar el PDF: " + e.message,
                    Toast.LENGTH_SHORT
            )
                .show()
        } finally {
            document.close()
        }
    }
}
