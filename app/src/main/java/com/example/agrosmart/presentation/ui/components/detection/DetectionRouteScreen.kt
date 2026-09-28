package com.example.agrosmart.presentation.ui.components.detection

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agrosmart.R
import com.example.agrosmart.core.utils.classes.ImageCacheManager
import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.core.utils.classes.NetworkChecker
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.presentation.navigation.DiagnosisInfoRoute
import com.example.agrosmart.presentation.viewmodels.state.DetectionUiState
import com.example.agrosmart.presentation.viewmodels.DetectionViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DetectionRouteScreen(
    resultArg: String?,
    imgPathArg: String?,
    onNavigateToCamera: () -> Unit,
    onNavigateToDiagnosisInfo: (DiagnosisInfoRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var lastDiagnosis by remember { mutableStateOf<DiagnosisHistoryListView?>(null) }
    var historyList by remember { mutableStateOf<List<DiagnosisHistoryListView>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var diagnosisToDelete by remember { mutableStateOf<String?>(null) }

    fun setHistoryIcons(nameCrop: String, nameDeficiency: String): IntArray {
        val resources = IntArray(2)
        when (nameCrop) {
            "Maiz" -> resources[0] = R.drawable.maiz
            "Frijol" -> resources[0] = R.drawable.frijoles_rojos
            else -> resources[0] = R.drawable.wheat
        }

        val lastSpace = nameDeficiency.lastIndexOf(" ")
        val deficiency = if (lastSpace == -1) nameDeficiency else nameDeficiency.substring(lastSpace + 1)

        when (deficiency.lowercase(Locale.ROOT)) {
            "nitrogeno" -> resources[1] = R.drawable.nitrogeno
            "fosforo" -> resources[1] = R.drawable.fosforo
            "magnesio" -> resources[1] = R.drawable.magnesio
            "potasio" -> resources[1] = R.drawable.potasio
            else -> resources[1] = R.drawable.cultivo_sano
        }

        return resources
    }

    fun diagnosisToHistoryLV(dh: DiagnosisHistory): DiagnosisHistoryListView {
        val cropName = dh.crop?.cropName ?: ""
        val resources = setHistoryIcons(cropName, dh.deficiency)
        val date = dh.diagnosisDate
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateFormatted = format.format(date)

        val history = DiagnosisHistoryListView()
        history.cropIcon = resources[0]
        history.deficiencyIcon = resources[1]
        history.id = dh._id
        history.txtDate = dateFormatted
        history.deficiency = dh.deficiency
        history.image = dh.image
        history.recommendation = dh.recommendation
        return history
    }

    LaunchedEffect(resultArg, imgPathArg) {
        if (!resultArg.isNullOrEmpty() && !imgPathArg.isNullOrEmpty()) {
            val imgBytes = ImageCacheManager.getArrayFromFile(imgPathArg)
            if (imgBytes != null && imgBytes.isNotEmpty()) {
                viewModel.saveDiagnosis(resultArg, imgBytes) { _ ->
                    viewModel.refreshData()
                }
                ImageCacheManager.cleanupCache(context)
                if (NetworkChecker.isInternetAvailable(context)) {
                    viewModel.obtenerRecomendacion(resultArg)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshData()
        viewModel.uiState.collect { state ->
            when (state) {
                is DetectionUiState.Idle -> {
                    isLoading = false
                }
                is DetectionUiState.Loading -> {
                    isLoading = true
                }
                is DetectionUiState.Success -> {
                    isLoading = false
                    historyList = state.histories.map { diagnosisToHistoryLV(it) }
                    lastDiagnosis = state.diagnosis?.let { diagnosisToHistoryLV(it) }
                }
                is DetectionUiState.Error -> {
                    isLoading = false
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onNavigateToCamera()
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun onCameraClick() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            onNavigateToCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    DetectionScreen(
        lastDiagnosis = lastDiagnosis,
        histories = historyList,
        isLoading = isLoading,
        onDiagnosisClick = { item ->
            val imgData = item.image
            onNavigateToDiagnosisInfo(
                DiagnosisInfoRoute(
                    idDiagnosis = item.id,
                    cropImage = if (imgData != null) ImageEncoder.encoderBase64(imgData) else "",
                    cropName = if (item.cropIcon == R.drawable.maiz) "Maiz" else if (item.cropIcon == R.drawable.frijoles_rojos) "Frijol" else "Cultivo",
                    diagnosisDate = item.txtDate,
                    diagnosisName = item.deficiency,
                    recommendation = item.recommendation.orEmpty()
                )
            )
        },
        onDeleteDiagnosis = { id ->
            diagnosisToDelete = id
        },
        onCameraClick = { onCameraClick() },
        modifier = modifier
    )

    if (diagnosisToDelete != null) {
        AlertDialog(
            onDismissRequest = { diagnosisToDelete = null },
            title = { Text("Eliminar Diagnóstico") },
            text = { Text("¿Está seguro de que desea eliminar este diagnóstico?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = diagnosisToDelete
                        if (id != null) {
                            viewModel.deleteHistory(id)
                        }
                        diagnosisToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { diagnosisToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
