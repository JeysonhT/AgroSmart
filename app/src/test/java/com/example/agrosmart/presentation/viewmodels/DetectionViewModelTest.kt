package com.example.agrosmart.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.models.Respuesta
import com.example.agrosmart.domain.usecase.CropsUseCase
import com.example.agrosmart.domain.usecase.DeleteDiagnosisUseCase
import com.example.agrosmart.domain.usecase.DetectionResultUseCase
import com.example.agrosmart.domain.usecase.DetectionUseCase
import com.example.agrosmart.domain.usecase.GetDiagnosisHistoryUseCase
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase
import com.example.agrosmart.domain.usecase.MMLStatsUseCase
import com.example.agrosmart.domain.usecase.SaveDiagnosisUseCase
import com.example.agrosmart.domain.usecase.UpdateDiagnosisRecommendationUseCase
import com.example.agrosmart.presentation.viewmodels.state.DetectionUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class DetectionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getDiagnosisHistoryUseCase: GetDiagnosisHistoryUseCase = mock(GetDiagnosisHistoryUseCase::class.java)
    private val saveDiagnosisUseCase: SaveDiagnosisUseCase = mock(SaveDiagnosisUseCase::class.java)
    private val deleteDiagnosisUseCase: DeleteDiagnosisUseCase = mock(DeleteDiagnosisUseCase::class.java)
    private val updateDiagnosisRecommendationUseCase: UpdateDiagnosisRecommendationUseCase = mock(UpdateDiagnosisRecommendationUseCase::class.java)
    private val getRecommendationUseCase: GetRecommendationUseCase = mock(GetRecommendationUseCase::class.java)
    private val cropsUseCase: CropsUseCase = mock(CropsUseCase::class.java)
    private val detectionUseCase: DetectionUseCase = mock(DetectionUseCase::class.java)
    private val mmlUseCase: MMLStatsUseCase = mock(MMLStatsUseCase::class.java)
    private val drUseCase: DetectionResultUseCase = mock(DetectionResultUseCase::class.java)

    private lateinit var viewModel: DetectionViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        `when`(getDiagnosisHistoryUseCase.invoke()).thenReturn(flowOf(emptyList()))

        viewModel = DetectionViewModel(
            getDiagnosisHistoryUseCase = getDiagnosisHistoryUseCase,
            saveDiagnosisUseCase = saveDiagnosisUseCase,
            deleteDiagnosisUseCase = deleteDiagnosisUseCase,
            updateDiagnosisRecommendationUseCase = updateDiagnosisRecommendationUseCase,
            getRecommendationUseCase = getRecommendationUseCase,
            cropsUseCase = cropsUseCase,
            detectionUseCase = detectionUseCase,
            mmlUseCase = mmlUseCase,
            drUseCase = drUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `obtenerRecomendacion updates recommendationResponse LiveData and UiState`() = runTest {
        val fakeResponse = Respuesta("Recomendación de prueba")
        `when`(getRecommendationUseCase.ejecutar(anyString()))
            .thenReturn(fakeResponse)

        val observer = mock(Observer::class.java) as Observer<Respuesta?>
        viewModel.getRecommendationResponse().observeForever(observer)

        viewModel.obtenerRecomendacion("Problema de fósforo")

        verify(observer).onChanged(fakeResponse)
        val state = viewModel.uiState.value
        assertTrue(state is DetectionUiState.Success)
        assertEquals("Recomendación de prueba", (state as DetectionUiState.Success).recommendation)
    }

    @Test
    fun `addNewHistory inserts at beginning of list and updates last diagnosis`() {
        val oldHistory = DiagnosisHistory.builder()
            ._id("1")
            .diagnosisDate(Date())
            .deficiency("Viejo")
            .build()

        val newHistory = DiagnosisHistory.builder()
            ._id("2")
            .diagnosisDate(Date())
            .deficiency("Nuevo")
            .build()

        viewModel.addNewHistory(oldHistory)
        viewModel.addNewHistory(newHistory)

        assertEquals("Nuevo", viewModel.getHistory().value?.get(0)?.deficiency)
        assertEquals(newHistory, viewModel.getLastDiagnosis().value)
    }

    @Test
    fun `cleanRecommendation sets recommendation LiveData to null`() = runTest {
        val fakeResponse = Respuesta("Test")
        `when`(getRecommendationUseCase.ejecutar(anyString()))
            .thenReturn(fakeResponse)
        viewModel.obtenerRecomendacion("Problema")

        viewModel.cleanRecommendation()

        assertNull(viewModel.getRecommendationResponse().value)
    }

    @Test
    fun `observeHistories collects flow and updates StateFlow and LiveData`() = runTest {
        val histories = listOf(
            DiagnosisHistory.builder()._id("1").deficiency("Deficiencia 1").build(),
            DiagnosisHistory.builder()._id("2").deficiency("Deficiencia 2").build()
        )
        `when`(getDiagnosisHistoryUseCase.invoke()).thenReturn(flowOf(histories))

        viewModel.observeHistories()

        assertEquals(2, viewModel.getHistory().value?.size)
        assertEquals("1", viewModel.getLastDiagnosis().value?.id)
        val state = viewModel.uiState.value
        assertTrue(state is DetectionUiState.Success)
        assertEquals(2, (state as DetectionUiState.Success).histories.size)
    }

    @Test
    fun `deleteHistory calls deleteDiagnosisUseCase`() = runTest {
        `when`(deleteDiagnosisUseCase.invoke("id-123")).thenReturn(Result.success(Unit))

        viewModel.deleteHistory("id-123")

        verify(deleteDiagnosisUseCase).invoke("id-123")
    }

    @Test
    fun `saveRecommendationInDiagnosis calls updateDiagnosisRecommendationUseCase`() = runTest {
        `when`(updateDiagnosisRecommendationUseCase.invoke("id-456", "Nueva recomendación"))
            .thenReturn(Result.success(Unit))

        viewModel.saveRecommendationInDiagnosis("id-456", "Nueva recomendación")

        verify(updateDiagnosisRecommendationUseCase).invoke("id-456", "Nueva recomendación")
    }
}
