package com.example.agrosmart.presentation.ui.components.crops

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agrosmart.R
import com.example.agrosmart.domain.designModels.CropCarouselData
import com.example.agrosmart.presentation.theme.AgroSmartTheme

/**
 * Tarjeta individual para mostrar un cultivo en el carrusel.
 * Reemplaza el ViewHolder e item_crop_image.xml tradicionales.
 */
@Composable
fun CropCard(
    crop: CropCarouselData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        // Contenedor de la imagen con esquinas redondeadas y elevación
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Image(
                painter = painterResource(id = crop.imageResource),
                contentDescription = crop.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Título del cultivo
        Text(
            text = crop.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Descripción del cultivo
        if (crop.description.isNotBlank()) {
            Text(
                text = crop.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Carrusel horizontal de cultivos.
 * Reemplaza el RecyclerView horizontal y su CropInfoAdapter.
 */
@Composable
fun CropsCarousel(
    crops: List<CropCarouselData>,
    onCropClick: (CropCarouselData) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            crops.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay cultivos disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = crops,
                        key = { crop -> "${crop.title}_${crop.type}_${crop.harvestTime}" }
                    ) { crop ->
                        CropCard(
                            crop = crop,
                            onClick = { onCropClick(crop) }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// PREVIEWS para Android Studio
// ==========================================

@Preview(showBackground = true)
@Composable
fun CropCardPreview() {
    AgroSmartTheme {
        Surface {
            CropCard(
                crop = CropCarouselData(
                    R.drawable.frijoles,
                    "Maíz",
                    "Cultivo principal de grano para consumo directo o forraje.",
                    "90 días",
                    "Gramínea"
                ),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CropsCarouselPreview() {
    AgroSmartTheme {
        Surface {
            val sampleCrops = listOf(
                CropCarouselData(R.drawable.imagen_1, "Maíz", "Cultivo de grano", "90 días", "Gramínea"),
                CropCarouselData(R.drawable.frijoles, "Frijol", "Leguminosa rica en proteína", "60 días", "Legumbre"),
                CropCarouselData(R.drawable.frijol, "Sorgo", "Resistente a la sequía", "100 días", "Cereal")
            )
            CropsCarousel(
                crops = sampleCrops,
                onCropClick = {}
            )
        }
    }
}