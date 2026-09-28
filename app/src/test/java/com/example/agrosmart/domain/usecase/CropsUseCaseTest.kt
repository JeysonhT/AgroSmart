package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.repository.CropRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class CropsUseCaseTest {

    private val cropRepository: CropRepository = mock(CropRepository::class.java)
    private lateinit var cropsUseCase: CropsUseCase

    @Before
    fun setUp() {
        cropsUseCase = CropsUseCase(cropRepository)
    }

    @Test
    fun testGetCrops_returnsRepositoryData() = runTest {
        val expectedCrops = listOf(Crop("Maiz", "descripcion", "12", "tipo"))
        `when`(cropRepository.getCrops()).thenReturn(expectedCrops)

        val result = cropsUseCase.getCrops()

        assertEquals(expectedCrops, result)
        verify(cropRepository).getCrops()
    }

    @Test
    fun testInvoke_returnsRepositoryData() = runTest {
        val expectedCrops = listOf(Crop("Frijol", "descripcion", "15", "tipo"))
        `when`(cropRepository.getCrops()).thenReturn(expectedCrops)

        val result = cropsUseCase()

        assertEquals(expectedCrops, result)
        verify(cropRepository).getCrops()
    }

    @Test
    fun testGetCropByName_returnsRepositoryData() = runTest {
        val expectedCrops = listOf(Crop("Maiz", "descripcion", "12", "tipo"))
        `when`(cropRepository.getCropByName("Maiz")).thenReturn(expectedCrops)

        val result = cropsUseCase.getCropByName("Maiz")

        assertEquals(expectedCrops, result)
        verify(cropRepository).getCropByName("Maiz")
    }
}
