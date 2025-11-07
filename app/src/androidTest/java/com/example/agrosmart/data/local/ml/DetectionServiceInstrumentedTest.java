package com.example.agrosmart.data.local.ml;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.agrosmart.data.local.dto.MMLResultDTO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tensorflow.lite.support.image.TensorImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DetectionServiceInstrumentedTest {

    private DetectionService detectionService;
    private Context context;

    @Before
    public void setUp() {
        // Obtenemos el contexto real de la aplicación bajo prueba.
        context = ApplicationProvider.getApplicationContext();
        detectionService = new DetectionService();
    }

    @Test
    public void processDetection_executesModelAndReturnsValidResult() throws IOException {
        // 1. Arrange (Preparar)

        // Creamos un Bitmap de prueba. En un caso real, podrías cargar una imagen desde los assets de prueba.
        // El tamaño (224x224) debe coincidir con el esperado por el modelo.
        Bitmap testBitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888);

        // Convertimos el Bitmap a TensorImage usando el método del propio servicio.
        TensorImage tensorImage = detectionService.bitmatToTensor(testBitmap);

        // Leemos las clases reales desde los assets para la validación.
        List<String> actualClasses = readClassesFromAssets();

        // 2. Act (Actuar)
        // Ejecutamos la detección. Esto llamará al modelo real de TFLite en el emulador/dispositivo.
        MMLResultDTO result = detectionService.processDetection(tensorImage, context);

        // 3. Assert (Verificar)
        assertNotNull("El resultado no debería ser nulo.", result);
        assertNotNull("El nombre de la clase detectada no debería ser nulo.", result.getResult());
        assertTrue("El tiempo de inferencia debe ser mayor que cero.", result.getInferenceTime() > 0);
        assertTrue("El uso de memoria debe ser registrado.", result.getMemoryUse() != 0); // Puede ser positivo o negativo
        assertTrue("La clase detectada debe estar en la lista de clases conocidas.", actualClasses.contains(result.getResult()));
    }

    private List<String> readClassesFromAssets() throws IOException {
        List<String> classes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open("clases.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            classes.add(line);
        }
        reader.close();
        return classes;
    }
}
