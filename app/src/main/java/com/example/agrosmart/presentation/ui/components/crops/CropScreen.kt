package com.example.agrosmart.presentation.ui.components.crops

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agrosmart.R
import com.example.agrosmart.domain.designModels.CropCarouselData
import com.example.agrosmart.presentation.theme.AgroSmartTheme

/**
 * Pantalla completa de detalle de cultivo en Jetpack Compose.
 * Reemplaza o complementa CropInfoFragment.
 */
@Composable
fun CropScreen(
    crop: CropCarouselData,
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
        // Cabecera con imagen y botón de regreso flotante
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            val imageRes = if (crop.imageResource > 0) crop.imageResource else R.drawable.frijol
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = crop.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )

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
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido con datos del cultivo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Título
            Text(
                text = crop.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fila de metadatos (Tipo y Tiempo de Cosecha)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (crop.type.isNotBlank()) {
                    CropBadge(label = "Tipo", value = crop.type)
                }
                if (crop.harvestTime.isNotBlank()) {
                    CropBadge(label = "Cosecha", value = crop.harvestTime)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de Descripción
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = crop.description.ifBlank { "Sin descripción disponible para este cultivo." },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Chip / Badge con diseño limpio para metadatos del cultivo.
 */
@Composable
private fun CropBadge(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CropScreenPreview() {
    AgroSmartTheme {
        CropScreen(
            crop = CropCarouselData(
                imageResource = R.drawable.frijoles,
                title = "Frijol Negro",
                description = "El frijol es una leguminosa fundamental en la dieta básica. Requiere suelos bien drenados y clima templado a cálido.",
                harvestTime = "60-90 días",
                type = "Leguminosa"
            )
        )
    }
}
