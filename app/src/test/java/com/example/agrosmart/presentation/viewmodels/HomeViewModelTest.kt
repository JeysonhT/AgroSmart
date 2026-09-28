package com.example.agrosmart.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.usecase.CropsUseCase
import com.example.agrosmart.presentation.viewmodels.state.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockCropsUseCase: CropsUseCase = mock(CropsUseCase::class.java)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(mockCropsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadCrops_Success() = runTest {
        // Given
        val crops = listOf(Crop("Maiz", "descripcion", "12", "tipo"))
        `when`(mockCropsUseCase.getCrops()).thenReturn(crops)

        // When
        viewModel.loadCrops()

        // Then
        assertNotNull(viewModel.crops.value)
        assertEquals(1, viewModel.crops.value?.size)
        assertEquals("Maiz", viewModel.crops.value?.get(0)?.title)
        assertTrue(viewModel.uiState.value is HomeUiState.Success)
        val successState = viewModel.uiState.value as HomeUiState.Success
        assertEquals(1, successState.crops.size)
        assertEquals("Maiz", successState.crops[0].title)
    }

    @Test
    fun testLoadCrops_Empty() = runTest {
        // Given
        `when`(mockCropsUseCase.getCrops()).thenReturn(emptyList())

        // When
        viewModel.loadCrops()

        // Then
        assertNotNull(viewModel.crops.value)
        assertEquals("No hay conexión a internet", viewModel.crops.value?.get(0)?.title)
        assertTrue(viewModel.uiState.value is HomeUiState.Success)
    }

    @Test
    fun testLoadCrops_Error() = runTest {
        // Given
        `when`(mockCropsUseCase.getCrops()).thenAnswer { throw RuntimeException("Error loading crops") }

        // When
        viewModel.loadCrops()

        // Then
        assertTrue(viewModel.uiState.value is HomeUiState.Error)
        val errorState = viewModel.uiState.value as HomeUiState.Error
        assertEquals("Error loading crops", errorState.message)
    }
}
