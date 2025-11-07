package com.example.agrosmart.presentation.ui.fragment;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.agrosmart.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Prueba de UI para CameraLayout que se enfoca en la intención de la galería,
 * omitiendo la lógica de navegación.
 */
@RunWith(AndroidJUnit4.class)
public class CameraLayoutTest {

    @Before
    public void setUp() {
        // Inicializa Espresso-Intents para poder interceptar y verificar intents.
        Intents.init();
    }

    @Test
    public void searchImageButton_firesGalleryIntent() {
        // 1. Arrange
        // Lanzamos el fragmento. Gracias al refactor, esto ya no crashea.
        FragmentScenario.launchInContainer(
                CameraLayout.class,
                null,
                R.style.Theme_AgroSmart,
                (FragmentFactory) null
        );

        // Opcional: Si no queremos que la prueba falle por un resultado inesperado,
        // podemos "stub" la intención para que siempre devuelva "cancelado".
        intending(hasAction(Intent.ACTION_GET_CONTENT))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null));

        // 2. Act
        // Hacemos clic en el botón de buscar imagen.
        onView(withId(R.id.search_image_button)).perform(click());

        // 3. Assert
        // Verificamos que se envió una intención con la acción ACTION_GET_CONTENT.
        intended(hasAction(Intent.ACTION_GET_CONTENT));
    }

    @After
    public void tearDown() {
        // Libera los recursos de Espresso-Intents.
        Intents.release();
    }
}
