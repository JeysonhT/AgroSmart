package com.example.agrosmart.presentation.ui.components.home

import androidx.annotation.RawRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.agrosmart.R
import com.example.agrosmart.domain.designModels.CropCarouselData
import com.example.agrosmart.presentation.theme.AgroSmartTheme
import com.example.agrosmart.presentation.theme.MidOrange
import com.example.agrosmart.presentation.ui.components.crops.CropsCarousel

/**
 * Componente reutilizable para reproducir animaciones Lottie en Jetpack Compose.
 *
 * @param rawRes Recurso raw del JSON de Lottie (ej: R.raw.notice_loading).
 * @param isPlaying Si la animación debe estar reproduciéndose o pausada.
 * @param iterations Cantidad de iteraciones (por defecto infinito en bucle).
 */
@Composable
fun LottieLoadingAnimation(
    @RawRes rawRes: Int,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    iterations: Int = LottieConstants.IterateForever
) {
    // 1. Carga la composición del JSON de forma asíncrona y segura
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))

    // 2. Anima el progreso de la composición controlando repeticiones y estado
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = iterations
    )

    // 3. Renderiza el frame de animación
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

/**
 * Pantalla principal Home en Jetpack Compose.
 * Reemplaza completamente el layout fragment_home_.xml.
 */
@Composable
fun HomeScreen(
    crops: List<CropCarouselData>,
    isLoadingCrops: Boolean,
    onCropClick: (CropCarouselData) -> Unit,
    onDeficienciesClick: () -> Unit,
    onFertilizersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ---------------------------------------------------------
        // 1. Sección Superior: Carrusel de Cultivos o Animación Lottie
        //    (Equivalente al Guideline 40% y cropsImageLoader en XML)
        // ---------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = isLoadingCrops,
                label = "CropsLoadingCrossfade"
            ) { loading ->
                if (loading) {
                    LottieLoadingAnimation(
                        rawRes = R.raw.notice_loading,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(180.dp)
                    )
                } else {
                    CropsCarousel(
                        crops = crops,
                        isLoading = false,
                        onCropClick = onCropClick
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------------------------------------------------
        // 2. Fila de Botones: Deficiencias y Fertilizantes
        //    (Equivalente a buttonDeficiencies y buttonFertilizers en XML)
        // ---------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de Deficiencias
            Button(
                onClick = onDeficienciesClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MidOrange,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cactus_24),
                    contentDescription = stringResource(id = R.string.buttonCactus),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(id = R.string.buttonCactus),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Botón de Fertilizantes
            Button(
                onClick = onFertilizersClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MidOrange,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.hoja_24),
                    contentDescription = stringResource(id = R.string.buttonLeaf),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(id = R.string.buttonLeaf),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ==========================================
// PREVIEWS para Android Studio
// ==========================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenLoadingPreview() {
    AgroSmartTheme {
        Surface {
            HomeScreen(
                crops = emptyList(),
                isLoadingCrops = true,
                onCropClick = {},
                onDeficienciesClick = {},
                onFertilizersClick = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenLoadedPreview() {
    AgroSmartTheme {
        Surface {
            val sampleCrops = listOf(
                CropCarouselData(R.drawable.imagen_1, "Maíz", "Cultivo principal de grano", "90 días", "Gramínea"),
                CropCarouselData(R.drawable.frijoles, "Frijol", "Leguminosa rica en proteína", "60 días", "Legumbre"),
                CropCarouselData(R.drawable.frijol, "Sorgo", "Resistente a climas áridos", "100 días", "Cereal")
            )
            HomeScreen(
                crops = sampleCrops,
                isLoadingCrops = false,
                onCropClick = {},
                onDeficienciesClick = {},
                onFertilizersClick = {}
            )
        }
    }
}