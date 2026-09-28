package com.example.agrosmart.presentation.navigation

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.agrosmart.core.utils.classes.ImageCacheManager
import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.core.utils.classes.NetworkChecker
import com.example.agrosmart.domain.designModels.CropCarouselData
import com.example.agrosmart.presentation.ui.components.crops.CropScreen
import com.example.agrosmart.presentation.ui.components.deficiencies.DeficienciesScreen
import com.example.agrosmart.presentation.ui.components.deficiencies.DeficiencyInfoScreen
import com.example.agrosmart.presentation.ui.components.detection.CameraRouteScreen
import com.example.agrosmart.presentation.ui.components.detection.DetectionRouteScreen
import com.example.agrosmart.presentation.ui.components.detection.DiagnosisInfoScreen
import com.example.agrosmart.presentation.ui.components.fertilizers.FertilizerInfoScreen
import com.example.agrosmart.presentation.ui.components.fertilizers.FertilizersScreen
import com.example.agrosmart.presentation.ui.components.home.HomeScreen
import com.example.agrosmart.presentation.ui.components.profile.ConfigScreen
import com.example.agrosmart.presentation.ui.components.profile.EditProfileRouteScreen
import com.example.agrosmart.presentation.ui.components.profile.PersonalDataRouteScreen
import com.example.agrosmart.presentation.ui.components.profile.ProfileRouteScreen
import com.example.agrosmart.presentation.viewmodels.DeficiencyViewModel
import com.example.agrosmart.presentation.viewmodels.DetectionViewModel
import com.example.agrosmart.presentation.viewmodels.FertilizerViewModel
import com.example.agrosmart.presentation.viewmodels.HomeViewModel
import com.example.agrosmart.presentation.viewmodels.state.DeficiencyUiState
import com.example.agrosmart.presentation.viewmodels.state.FertilizerUiState
import com.example.agrosmart.presentation.viewmodels.state.HomeUiState
import androidx.compose.runtime.collectAsState
import androidx.core.content.edit

@Composable
fun AgroSmartNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier 
    ) {
        // 1. Home
        composable<HomeRoute> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                homeViewModel.loadCrops()
            }

            val crops = (uiState as? HomeUiState.Success)?.crops.orEmpty()
            val isLoading = uiState is HomeUiState.Loading || uiState is HomeUiState.Idle

            HomeScreen(
                crops = crops,
                isLoadingCrops = isLoading,
                onCropClick = { crop ->
                    navController.navigate(
                        CropInfoRoute(
                            image = crop.imageResource,
                            title = crop.title,
                            description = crop.description,
                            harvestTime = crop.harvestTime,
                            type = crop.type
                        )
                    )
                },
                onDeficienciesClick = { navController.navigate(DeficienciesRoute) },
                onFertilizersClick = { navController.navigate(FertilizersRoute) }
            )
        }

        // 2. Crop Detail
        composable<CropInfoRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CropInfoRoute>()
            CropScreen(
                crop = CropCarouselData(
                    route.image,
                    route.title,
                    route.description,
                    route.harvestTime,
                    route.type
                ),
                onBackClick = { navController.popBackStack() }
            )
        }

        // 3. Deficiencies
        composable<DeficienciesRoute> {
            val context = LocalContext.current
            val viewModel: DeficiencyViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadData(context)
            }

            val deficiencies = (uiState as? DeficiencyUiState.Success)?.deficiencies.orEmpty()
            val isLoading = uiState is DeficiencyUiState.Loading || uiState is DeficiencyUiState.Idle

            DeficienciesScreen(
                deficiencies = deficiencies,
                isLoading = isLoading,
                onDeficiencyClick = { deficiency ->
                    try {
                        val imageUri = ImageCacheManager.saveImageToCache(context, deficiency.imageResource)
                        navController.navigate(
                            DeficiencyInfoRoute(
                                imageuri = imageUri.orEmpty(),
                                deficiencyName = deficiency.name,
                                description = deficiency.description,
                                symptoms = deficiency.symptoms,
                                solutions = deficiency.solutions
                            )
                        )
                    } catch (e: Exception) {
                        navController.navigate(
                            DeficiencyInfoRoute(
                                imageuri = "",
                                deficiencyName = deficiency.name,
                                description = deficiency.description,
                                symptoms = deficiency.symptoms,
                                solutions = deficiency.solutions
                            )
                        )
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 4. Deficiency Detail
        composable<DeficiencyInfoRoute> { backStackEntry ->
            val context = LocalContext.current
            val route = backStackEntry.toRoute<DeficiencyInfoRoute>()
            val imageBitmap = remember(route.imageuri) {
                if (route.imageuri.isNotBlank()) {
                    ImageCacheManager.loadImageFromCache(route.imageuri)?.asImageBitmap()
                } else null
            }

            DisposableEffect(Unit) {
                onDispose {
                    ImageCacheManager.cleanupCache(context)
                }
            }

            DeficiencyInfoScreen(
                name = route.deficiencyName,
                description = route.description,
                symptoms = route.symptoms,
                solutions = route.solutions,
                imageBitmap = imageBitmap,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 5. Fertilizers
        composable<FertilizersRoute> {
            val context = LocalContext.current
            val viewModel: FertilizerViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadData(context)
            }

            val fertilizers = (uiState as? FertilizerUiState.Success)?.fertilizers.orEmpty()
            val isLoading = uiState is FertilizerUiState.Loading || uiState is FertilizerUiState.Idle

            FertilizersScreen(
                fertilizers = fertilizers,
                isLoading = isLoading,
                onFertilizerClick = { fertilizer ->
                    try {
                        val imageUri = ImageCacheManager.saveImageToCache(context, fertilizer.imageResource)
                        navController.navigate(
                            FertilizerInfoRoute(
                                imageuri = imageUri.orEmpty(),
                                name = fertilizer.name ?: "",
                                description = fertilizer.description ?: "",
                                type = fertilizer.type ?: "",
                                provider = fertilizer.supplier ?: "",
                                applicationMethod = fertilizer.applicationMethod ?: "",
                                recommendedDose = fertilizer.recommendedDose ?: ""
                            )
                        )
                    } catch (e: Exception) {
                        navController.navigate(
                            FertilizerInfoRoute(
                                imageuri = "",
                                name = fertilizer.name ?: "",
                                description = fertilizer.description ?: "",
                                type = fertilizer.type ?: "",
                                provider = fertilizer.supplier ?: "",
                                applicationMethod = fertilizer.applicationMethod ?: "",
                                recommendedDose = fertilizer.recommendedDose ?: ""
                            )
                        )
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 6. Fertilizer Detail
        composable<FertilizerInfoRoute> { backStackEntry ->
            val context = LocalContext.current
            val route = backStackEntry.toRoute<FertilizerInfoRoute>()
            val imageBitmap = remember(route.imageuri) {
                if (route.imageuri.isNotBlank()) {
                    ImageCacheManager.loadImageFromCache(route.imageuri)?.asImageBitmap()
                } else null
            }

            DisposableEffect(Unit) {
                onDispose {
                    ImageCacheManager.cleanupCache(context)
                }
            }

            FertilizerInfoScreen(
                name = route.name,
                description = route.description,
                type = route.type,
                supplier = route.provider,
                applicationMethod = route.applicationMethod,
                recommendedDose = route.recommendedDose,
                imageBitmap = imageBitmap,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 7. Detection
        composable<DetectionRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DetectionRoute>()
            DetectionRouteScreen(
                resultArg = route.result,
                imgPathArg = route.imgPath,
                onNavigateToCamera = { navController.navigate(CameraRoute) },
                onNavigateToDiagnosisInfo = { diagnosisRoute ->
                    navController.navigate(diagnosisRoute)
                }
            )
        }

        // 8. Camera
        composable<CameraRoute> {
            val detectionViewModel: DetectionViewModel = hiltViewModel()
            CameraRouteScreen(
                viewModel = detectionViewModel,
                onDetectionComplete = { result, cachedPath ->
                    navController.navigate(DetectionRoute(result = result, imgPath = cachedPath)) {
                        popUpTo<DetectionRoute> { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 9. Diagnosis Detail
        composable<DiagnosisInfoRoute> { backStackEntry ->
            val context = LocalContext.current
            val route = backStackEntry.toRoute<DiagnosisInfoRoute>()
            val detectionViewModel: DetectionViewModel = hiltViewModel()

            var recommendationText by remember { mutableStateOf(route.recommendation) }
            var isLoadingRecommendation by remember { mutableStateOf(false) }

            val imageBitmap = remember(route.cropImage) {
                if (route.cropImage.isNotBlank()) {
                    try {
                        val bytes = ImageEncoder.decoderBase64(route.cropImage)
                        if (bytes != null) BitmapFactory.decodeByteArray(bytes, 0, bytes.size) else null
                    } catch (_: Exception) {
                        null
                    }
                } else null
            }

            DiagnosisInfoScreen(
                cropName = route.cropName,
                diagnosisDate = route.diagnosisDate,
                deficiencyName = route.diagnosisName,
                recommendation = recommendationText,
                imageBitmap = imageBitmap,
                isLoading = isLoadingRecommendation,
                onGenerateRecommendationClick = {
                    if (NetworkChecker.isInternetAvailable(context)) {
                        isLoadingRecommendation = true
                        detectionViewModel.obtenerRecomendacion(route.diagnosisName)
                    } else {
                        Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 10. Profile
        composable<ProfileRoute> {
            ProfileRouteScreen(
                onNavigateToEditProfile = { username ->
                    navController.navigate(EditProfileRoute(username = username))
                },
                onNavigateToPersonalData = { navController.navigate(PersonalDataRoute) },
                onNavigateToConfig = { navController.navigate(ConfigRoute) }
            )
        }

        // 11. Edit Profile
        composable<EditProfileRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<EditProfileRoute>()
            EditProfileRouteScreen(
                usernameArg = route.username,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 12. Config
        composable<ConfigRoute> {
            val context = LocalContext.current
            val prefs = remember {
                context.getSharedPreferences("agrosmart_config_prefs", Context.MODE_PRIVATE)
            }
            var autoSave by remember {
                mutableStateOf(prefs.getBoolean("auto_save_recommendations", true))
            }

            ConfigScreen(
                autoSaveRecommendations = autoSave,
                onAutoSaveRecommendationsChange = { isChecked ->
                    autoSave = isChecked
                    prefs.edit {
                        putBoolean(
                                "auto_save_recommendations",
                                isChecked
                        )
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 13. Personal Data
        composable<PersonalDataRoute> {
            PersonalDataRouteScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
