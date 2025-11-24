package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.data.network.DetectionResultService;
import com.example.agrosmart.domain.models.DetectionResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DetectionResultUseCaseTest {

    @Mock
    private DetectionResultService mockDetectionResultService;

    private DetectionResultUseCase detectionResultUseCase;

    @Before
    public void setUp() {
        detectionResultUseCase = new DetectionResultUseCase(mockDetectionResultService);
    }

    @Test
    public void saveResult_shouldReturnTrue_whenSaveIsSuccessful() throws Exception {
        // Given
        DetectionResult detectionResult = new DetectionResult("someImage64", "someResult");
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.complete(true);
        when(mockDetectionResultService.saveDetectionResult(detectionResult)).thenReturn(future);

        // When
        CompletableFuture<Boolean> resultFuture = detectionResultUseCase.saveResult(detectionResult);
        boolean result = resultFuture.get();

        // Then
        assertTrue(result);
        verify(mockDetectionResultService).saveDetectionResult(detectionResult);
    }

    @Test
    public void saveResult_shouldReturnFalse_whenSaveFails() throws Exception {
        // Given
        DetectionResult detectionResult = new DetectionResult("someImage64", "someResult");
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        future.complete(false);
        when(mockDetectionResultService.saveDetectionResult(detectionResult)).thenReturn(future);

        // When
        CompletableFuture<Boolean> resultFuture = detectionResultUseCase.saveResult(detectionResult);
        boolean result = resultFuture.get();

        // Then
        assertFalse(result);
        verify(mockDetectionResultService).saveDetectionResult(detectionResult);
    }
}