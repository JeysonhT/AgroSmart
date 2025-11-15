package com.example.agrosmart.core.utils.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.Toast;

import com.example.agrosmart.domain.models.DiagnosisHistory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfGenerator {

    private static final int PAGE_WIDTH = 595; // Ancho A4 en puntos
    private static final int PAGE_HEIGHT = 842; // Alto A4 en puntos
    private static final int MARGIN = 40;

    public static void generateDiagnosisHistoryPdf(Context context, List<DiagnosisHistory> historyList) {
        if (historyList == null || historyList.isEmpty()) {
            Toast.makeText(context, "No hay datos para generar el PDF.", Toast.LENGTH_SHORT).show();
            return;
        }

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        TextPaint titlePaint = new TextPaint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(18);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        TextPaint headerPaint = new TextPaint();
        headerPaint.setColor(Color.BLACK);
        headerPaint.setTextSize(12);
        headerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        TextPaint bodyPaint = new TextPaint();
        bodyPaint.setColor(Color.DKGRAY);
        bodyPaint.setTextSize(11);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.LTGRAY);
        linePaint.setStrokeWidth(1);

        int y = MARGIN;

        // Título del documento
        canvas.drawText("Historial de Diagnósticos Guardados", MARGIN, y, titlePaint);
        y += 40;

        for (DiagnosisHistory history : historyList) {
            // Si el contenido no cabe, crea una nueva página
            if (y > PAGE_HEIGHT - MARGIN * 2) { // Margen inferior
                document.finishPage(page);
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
                y = MARGIN;
            }

            // Dibujar línea separadora
            canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint);
            y += 20;

            // Contenido del diagnóstico
            canvas.drawText("Cultivo: " + history.getCrop().getCropName(), MARGIN, y, headerPaint);
            y += 15;
            canvas.drawText("Enfermedad: " + history.getDeficiency(), MARGIN, y, headerPaint);
            y += 15;
            canvas.drawText("Fecha: " + history.getDiagnosisDate(), MARGIN, y, bodyPaint);
            y += 20;

            // Para el texto de recomendación, usamos StaticLayout para manejar saltos de línea
            canvas.save();
            canvas.translate(MARGIN, y);
            StaticLayout recommendationLayout = new StaticLayout(
                    "Recomendación: " + history.getRecommendation(),
                    bodyPaint,
                    PAGE_WIDTH - MARGIN * 2,
                    StaticLayout.Alignment.ALIGN_NORMAL,
                    1.0f,
                    0.0f,
                    false
            );
            recommendationLayout.draw(canvas);
            y += recommendationLayout.getHeight() + 20; // Añadir espacio después del texto
            canvas.restore();
        }

        document.finishPage(page);

        // Guardar el archivo
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "AgroSmart_Diagnosticos_" + timeStamp + ".pdf";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar el PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            document.close();
        }
    }
}
