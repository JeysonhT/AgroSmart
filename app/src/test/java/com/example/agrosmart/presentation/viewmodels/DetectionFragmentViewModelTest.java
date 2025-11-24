package com.example.agrosmart.presentation.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase;
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.timeout;

@RunWith(MockitoJUnitRunner.class)
public class DetectionFragmentViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetRecommendationUseCase getRecommendationUseCase;

    @Mock
    private DiagnosisHistoryUseCase diagnosisHistoryUseCase;

    @Mock
    private CropsUseCase cropsUseCase;

    @Mock
    private Observer<List<DiagnosisHistory>> historiesObserver;

    @Mock
    private Observer<DiagnosisHistory> lastDiagnosisObserver;

    private DetectionFragmentViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new DetectionFragmentViewModel(getRecommendationUseCase, diagnosisHistoryUseCase, cropsUseCase);
        viewModel.getHistory().observeForever(historiesObserver);
        viewModel.getLastDiagnosis().observeForever(lastDiagnosisObserver);
    }

    @Test
    public void getHistoriesFromUseCaseTest() {
        // Given
        List<DiagnosisHistory> fakeHistories = new ArrayList<>();
        DiagnosisHistory diagnosis1 = DiagnosisHistory.builder()._id("1").build();
        DiagnosisHistory diagnosis2 = DiagnosisHistory.builder()._id("2").build();
        fakeHistories.add(diagnosis1);
        fakeHistories.add(diagnosis2);
        CompletableFuture<List<DiagnosisHistory>> future = new CompletableFuture<>();
        future.complete(fakeHistories);

        when(diagnosisHistoryUseCase.getHistories()).thenReturn(future);

        // When
        viewModel.gethistoriesFromUseCase();

        // Then
        verify(historiesObserver, timeout(100)).onChanged(fakeHistories);
        verify(lastDiagnosisObserver, timeout(100)).onChanged(diagnosis1); // Assuming the first item is the last diagnosis
    }

    @Test
    public void getHistoriesFromUseCase_shouldHandleError() {
        // Given
        String errorMessage = "Test error";
        RuntimeException testException = new RuntimeException(errorMessage);

        // Crear un mock que ejecute inmediatamente
        CompletableFuture<List<DiagnosisHistory>> mockFuture = Mockito.mock(CompletableFuture.class);

        when(diagnosisHistoryUseCase.getHistories()).thenReturn(mockFuture);
        when(mockFuture.thenAccept(any())).thenAnswer(invocation -> {
            Consumer<List<DiagnosisHistory>> consumer = invocation.getArgument(0);
            // Simular el exceptionally siendo llamado
            return mockFuture;
        });
        when(mockFuture.exceptionally(any())).thenAnswer(invocation -> {
            Function<Throwable, Void> function = invocation.getArgument(0);
            function.apply(testException); // Ejecutar inmediatamente la función de error
            return null;
        });

        // When
        viewModel.gethistoriesFromUseCase();

        // Then
        verify(historiesObserver, timeout(1000)).onChanged(Collections.emptyList());
        verify(lastDiagnosisObserver, timeout(1000)).onChanged(null);
    }

    @Test
    public void getHistoriesFromUseCase_shouldHandleEmptyList() {
        // Given
        List<DiagnosisHistory> emptyHistories = new ArrayList<>();
        CompletableFuture<List<DiagnosisHistory>> future = new CompletableFuture<>();
        future.complete(emptyHistories);

        when(diagnosisHistoryUseCase.getHistories()).thenReturn(future);

        // When
        viewModel.gethistoriesFromUseCase();

        // Then
        verify(historiesObserver, timeout(100)).onChanged(emptyHistories);
        verify(lastDiagnosisObserver, timeout(100)).onChanged(null); // lastDiagnosis should be null if list is empty
    }

    @Test
    public void saveRecommendationInDiagnosisTest() {
        // Given
        String diagnosisId = "test-id";
        String recommendation = "test-recommendation";

        // When
        viewModel.saveRecommendationInDiagnosis(diagnosisId, recommendation);

        // Then
        verify(diagnosisHistoryUseCase).updateDiagnosis(diagnosisId, recommendation);
    }
}
