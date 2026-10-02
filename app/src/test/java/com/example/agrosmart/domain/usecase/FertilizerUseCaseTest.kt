package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Fertilizer
import com.example.agrosmart.domain.repository.FertilizerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class FertilizerUseCaseTest {

    private val fertilizerRepository: FertilizerRepository = mock(FertilizerRepository::class.java)
    private lateinit var fertilizerUseCase: FertilizerUseCase

    @Before
    fun setUp() {
        fertilizerUseCase = FertilizerUseCase(fertilizerRepository)
    }

    @Test
    fun testInvoke_returnsFertilizersFromRepository() = runTest {
        val expectedFertilizers = listOf(
            Fertilizer(
                id = "1",
                name = "Urea",
                description = "Fertilizante nitrogenado",
                type = "Químico"
            )
        )
        `when`(fertilizerRepository.getFertilizers()).thenReturn(expectedFertilizers)

        val result = fertilizerUseCase()

        assertEquals(expectedFertilizers, result)
        verify(fertilizerRepository).getFertilizers()
    }
}
