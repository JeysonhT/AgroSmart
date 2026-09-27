package com.example.agrosmart.presentation.ui.components.fertilizers

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
 * Pantalla de detalle de un fertilizante en Jetpack Compose.
 */
@Composable
fun FertilizerInfoScreen(
    name: String,
    description: String,
    type: String,
    supplier: String,
    applicationMethod: String,
    recommendedDose: String,
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
                    painter = painterResource(id = R.drawable.gemini_fertilizer_placeholder),
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

        // Contenido con datos del fertilizante
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Nombre del fertilizante
            Text(
                text = name.ifBlank { "Fertilizante" },
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Chips de metadatos (Tipo y Proveedor)
            val hasType = type.isNotBlank()
            val hasSupplier = supplier.isNotBlank()

            if (hasType || hasSupplier) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasType) {
                        FertilizerBadge(label = "Tipo", value = type)
                    }
                    if (hasSupplier) {
                        FertilizerBadge(label = "Proveedor", value = supplier)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección: Descripción
            if (description.isNotBlank()) {
                FertilizerInfoSection(
                    title = stringResource(id = R.string.descripci_nTxt),
                    content = description
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sección: Método de aplicación
            if (applicationMethod.isNotBlank()) {
                FertilizerInfoSection(
                    title = stringResource(id = R.string.fertilizerInfoAMethodText),
                    content = applicationMethod
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sección: Dosis recomendada
            if (recommendedDose.isNotBlank()) {
                FertilizerInfoSection(
                    title = stringResource(id = R.string.fertilizerInfoRDoseText),
                    content = recommendedDose
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Chip / Badge con diseño limpio para metadatos del fertilizante.
 */
@Composable
private fun FertilizerBadge(label: String, value: String) {
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

/**
 * Tarjeta reusable para mostrar secciones informativas de manera limpia.
 */
@Composable
private fun FertilizerInfoSection(
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
fun FertilizerInfoScreenPreview() {
    AgroSmartTheme {
        FertilizerInfoScreen(
            name = "Urea 46%",
            description = "Fertilizante granulado con la mayor concentración de nitrógeno asimilable por los cultivos.",
            type = "Químico Nitrogenado",
            supplier = "Fertilizantes del Valle",
            applicationMethod = "Al voleo o incorporado al suelo cerca del sistema radicular antes del riego.",
            recommendedDose = "150 - 200 kg por hectárea según etapa del cultivo."
        )
    }
}
