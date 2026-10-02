package com.example.agrosmart.presentation.ui.components.detection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrosmart.R
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView
import com.example.agrosmart.presentation.theme.AgroSmartTheme
import com.example.agrosmart.presentation.theme.AppleGreen

/**
 * Tarjeta del último diagnóstico realizado.
 */
@Composable
fun LastDiagnosisCard(
    diagnosis: DiagnosisHistoryListView?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = diagnosis != null, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppleGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (diagnosis != null) {
                // Fila superior: Icono de cultivo, icono de deficiencia y etiqueta de diagnóstico
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (diagnosis.cropIcon > 0) {
                        Image(
                            painter = painterResource(id = diagnosis.cropIcon),
                            contentDescription = "Crop Icon",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (diagnosis.deficiencyIcon > 0) {
                        Image(
                            painter = painterResource(id = diagnosis.deficiencyIcon),
                            contentDescription = "Deficiency Icon",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(id = R.string.DiagnosisCard),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.3f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Fila inferior: Fecha y nombre de la deficiencia
                Text(
                    text = diagnosis.txtDate,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = diagnosis.deficiency,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                // Estado vacío cuando no hay diagnósticos previos
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.lastDeficiencyCardTextInNullCase),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Item individual del historial de diagnósticos.
 * Reemplaza item_history_detection.xml y el ViewHolder del adapter.
 */
@Composable
fun DiagnosisHistoryCard(
    history: DiagnosisHistoryListView,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de cultivo
            if (history.cropIcon > 0) {
                Image(
                    painter = painterResource(id = history.cropIcon),
                    contentDescription = "Crop Icon",
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            // Icono de deficiencia
            if (history.deficiencyIcon > 0) {
                Image(
                    painter = painterResource(id = history.deficiencyIcon),
                    contentDescription = "Deficiency Icon",
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            // Información: Fecha y Deficiencia
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = history.txtDate,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (history.deficiency.isNotBlank()) {
                    Text(
                        text = history.deficiency,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Botón de eliminar diagnóstico
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Image(
                    imageVector = DetectionIcons.icons.Delete,
                    contentDescription = "Eliminar diagnóstico",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/**
 * Pantalla completa de Detección e Historial en Jetpack Compose.
 * Reemplaza completamente fragment_detection.xml y DiagnosisHistoryAdapter.
 */
@Composable
fun DetectionScreen(
    lastDiagnosis: DiagnosisHistoryListView?,
    histories: List<DiagnosisHistoryListView>,
    isLoading: Boolean,
    onDiagnosisClick: (DiagnosisHistoryListView) -> Unit,
    onDeleteDiagnosis: (String) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCameraClick,
                containerColor = AppleGreen,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = DetectionIcons.icons.PhotoCamera,
                    contentDescription = stringResource(id = R.string.adddetection),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Spacer(modifier = Modifier.height(12.dp))

            // Título de la pantalla
            Text(
                text = stringResource(id = R.string.detection_fragment_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tarjeta del Último Diagnóstico
            LastDiagnosisCard(
                diagnosis = lastDiagnosis,
                onClick = {
                    lastDiagnosis?.let { onDiagnosisClick(it) }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Título de la sección de Historial
            Text(
                text = stringResource(id = R.string.historialString),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Contenido del Historial: Cargando, Vacío o Lista LazyColumn
            when {
                isLoading && histories.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AppleGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                histories.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no tienes registros en el historial.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = histories,
                            key = { it.id }
                        ) { item ->
                            DiagnosisHistoryCard(
                                onClick = { onDiagnosisClick(item) },
                                history = item,
                                onDelete = { onDeleteDiagnosis(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetectionScreenPreview() {
    AgroSmartTheme {
        Surface {
            val sampleItem = DiagnosisHistoryListView(
                cropIcon = R.drawable.maiz,
                deficiencyIcon = R.drawable.nitrogeno,
                id = "1",
                txtDate = "26/09/2026 14:30",
                deficiency = "Deficiencia de Nitrogeno",
                image = null,
                _recommendation = "Aplicar fertilizante rico en nitrógeno."
            )
            DetectionScreen(
                lastDiagnosis = sampleItem,
                histories = listOf(sampleItem, sampleItem.copy(id = "2", txtDate = "25/09/2026 10:15")),
                isLoading = false,
                onDiagnosisClick = {},
                onDeleteDiagnosis = {},
                onCameraClick = {}
            )
        }
    }
}

object DetectionIcons{
    val icons = Icons.Outlined
}
