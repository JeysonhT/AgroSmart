package com.example.agrosmart.domain.usecase;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.agrosmart.data.network.MMLStatsService;
import com.example.agrosmart.domain.models.MMLStats;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class MMLStatsUseCaseTest {

    @Mock
    private MMLStatsService mockMmlStatsService;

    @InjectMocks
    private MMLStatsUseCase mmlStatsUseCase;

    private MMLStats dummyStats;

    @Before
    public void setUp() {
        dummyStats = new MMLStats(100L, 2048L, Collections.singletonList(0.95f));
    }

    @Test
    public void saveStats_shouldCallServiceAndCompleteSuccessfully() {
        // Arrange
        when(mockMmlStatsService.saveStats(any(MMLStats.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Act
        mmlStatsUseCase.saveStats(dummyStats);

        // Assert
        verify(mockMmlStatsService).saveStats(dummyStats);
    }

    @Test
    public void saveStats_whenServiceThrowsException_shouldThrowRuntimeException() {
        // Arrange
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeExceptionally(new ExecutionException("Service error", new Throwable()));
        when(mockMmlStatsService.saveStats(any(MMLStats.class))).thenReturn(future);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mmlStatsUseCase.saveStats(dummyStats);
        });

        verify(mockMmlStatsService).saveStats(dummyStats);
    }

    @Test
    public void saveStats_whenServiceReturnsNull_shouldThrowRuntimeException() {
        // Arrange
        when(mockMmlStatsService.saveStats(any(MMLStats.class))).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mmlStatsUseCase.saveStats(dummyStats);
        });

        verify(mockMmlStatsService).saveStats(dummyStats);
    }
}
