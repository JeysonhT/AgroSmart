package com.example.agrosmart.presentation.ui.components.deficiencies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agrosmart.R
import com.example.agrosmart.presentation.theme.AgroSmartTheme

/**
 * Pantalla de detalle de una deficiencia nutricional en Jetpack Compose.
 */
@Composable
fun DeficiencyInfoScreen(
    name: String,
    description: String,
    symptoms: String,
    solutions: String,
    imageBitmap: ImageBitmap? = null,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // Cabecera con imagen y botón flotante de regreso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.gemini_deficiendy_image),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )
            }

            // Botón de regreso con fondo translúcido
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 40.dp, start = 16.dp)
                    .size(42.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.angulo_izquierdo_24),
                    contentDescription = stringResource(id = R.string.Back_Button),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido textual y secciones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Nombre de la deficiencia
            Text(
                text = name.ifBlank { "Deficiencia" },
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección: Descripción
            if (description.isNotBlank()) {
                InfoSectionCard(
                    title = stringResource(id = R.string.deficiencyDescriptionText),
                    content = description
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sección: Síntomas
            if (symptoms.isNotBlank()) {
                InfoSectionCard(
                    title = stringResource(id = R.string.deficiencySymptomsText),
                    content = symptoms
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sección: Soluciones
            if (solutions.isNotBlank()) {
                InfoSectionCard(
                    title = stringResource(id = R.string.deficiencySolutionsText),
                    content = solutions
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Tarjeta reusable para mostrar secciones informativas de manera limpia.
 */
@Composable
private fun InfoSectionCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeficiencyInfoScreenPreview() {
    AgroSmartTheme {
        DeficiencyInfoScreen(
            name = "Deficiencia de Nitrógeno",
            description = "El nitrógeno es esencial para el desarrollo vegetativo y la producción de clorofila.",
            symptoms = "Clorosis uniforme comenzando en las hojas basales más viejas y avanzando hacia arriba.",
            solutions = "Aplicar fertilizantes nitrogenados como urea o sulfato de amonio según el análisis de suelo."
        )
    }
}
