package com.example.agrosmart.presentation.ui.components.detection

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.agrosmart.R
import com.example.agrosmart.presentation.theme.GrassGreen

/**
 * Pantalla de captura fotográfica y selección de imagen con CameraX en Jetpack Compose.
 */
@Composable
fun CameraScreen(
    onPreviewViewReady: (PreviewView) -> Unit,
    onCaptureClick: () -> Unit,
    onSearchImageClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isProcessing: Boolean = false,
    errorMessage: String? = null,
    onDismissError: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Vista previa de la cámara (CameraX PreviewView)
        AndroidView(
            factory = { context ->
                PreviewView(context).also { previewView ->
                    onPreviewViewReady(previewView)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Botón de regreso superior
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .size(44.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.angulo_izquierdo_24),
                contentDescription = stringResource(id = R.string.Back_Button),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Overlay inferior con controles de cámara
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    color = GrassGreen,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Procesando diagnóstico...",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color.White
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón para buscar imagen desde galería
                    FilledTonalButton(
                        onClick = onSearchImageClick,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.anadir_imagen_24),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.BuscarPhotoString),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    // Botón disparador para tomar foto (Directo, sin Row anidada)
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .border(width = 4.dp, color = Color.White, shape = CircleShape)
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(GrassGreen)
                            .clickable(onClick = onCaptureClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camara_24),
                            contentDescription = stringResource(id = R.string.takephotoString),
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }

    // Diálogo de error
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = onDismissError,
            title = {
                Text(
                    text = "Resultado de diagnóstico",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = onDismissError) {
                    Text("Aceptar", color = GrassGreen, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
