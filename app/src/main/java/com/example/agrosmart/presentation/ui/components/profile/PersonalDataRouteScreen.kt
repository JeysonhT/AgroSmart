package com.example.agrosmart.presentation.ui.components.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agrosmart.core.utils.classes.PdfGenerator
import com.example.agrosmart.presentation.viewmodels.PersonalDataViewModel
import com.example.agrosmart.presentation.viewmodels.state.PersonalDataUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PersonalDataRouteScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonalDataViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val successState = uiState as? PersonalDataUiState.Success
    val listOfCrops = successState?.topCrops.orEmpty()
    val listOfDeficiencies = successState?.topDeficiencies.orEmpty()
    val diagnosisGenerated = successState?.diagnosisGenerated ?: 0
    val recommendationsSaved = successState?.recommendationsSaved ?: 0
    val historyList = successState?.diagnosisHistory.orEmpty()
    val isLoading = uiState is PersonalDataUiState.Loading

    val createCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.exportDiagnosisHistoryToCsv(context, uri)
            Toast.makeText(context, "Exportando datos a CSV...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error al crear el archivo CSV.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadDiagnosisHistory()
    }

    PersonalDataScreen(
        crops = listOfCrops,
        deficiencies = listOfDeficiencies,
        diagnosisCount = diagnosisGenerated,
        recommendationsSavedCount = recommendationsSaved,
        onExportPdfClick = {
            PdfGenerator.generateDiagnosisHistoryPdf(context, historyList)
        },
        onExportCsvClick = {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            createCsvLauncher.launch("AgroSmart_Historial_$timeStamp.csv")
        },
        onImportCsvClick = {
            Toast.makeText(context, "Importación no disponible", Toast.LENGTH_SHORT).show()
        },
        onBackClick = onBackClick,
        isLoading = isLoading,
        modifier = modifier
    )
}
