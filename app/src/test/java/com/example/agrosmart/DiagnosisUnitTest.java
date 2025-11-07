package com.example.agrosmart;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase;
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


public class DiagnosisUnitTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private GetRecommendationUseCase mockRecommendationUseCase;
    private DiagnosisHistoryUseCase mockDiagnosisUseCase;
    private CropsUseCase mockCropsUseCase;
    private DetectionFragmentViewModel viewModel;

    @Before
    public void setUp() {
        mockRecommendationUseCase = mock(GetRecommendationUseCase.class);
        mockDiagnosisUseCase = mock(DiagnosisHistoryUseCase.class);
        mockCropsUseCase = mock(CropsUseCase.class);

        viewModel = new DetectionFragmentViewModel(
                mockRecommendationUseCase,
                mockDiagnosisUseCase,
                mockCropsUseCase
        );
    }

    @Test
    public void obtenerRecomendacion_postsValueToLiveData() {
        // Arrange
        Respuesta fakeResponse = new Respuesta("Recomendación de prueba");
        when(mockRecommendationUseCase.ejecutar(anyString()))
                .thenReturn(CompletableFuture.completedFuture(fakeResponse));

        Observer<Respuesta> observer = mock(Observer.class);
        viewModel.getRecommendationResponse().observeForever(observer);

        // Act
        viewModel.obtenerRecomendacion("Problema en la planta");

        // Assert
        verify(observer).onChanged(fakeResponse);
    }


    @Test
    public void addNewHistory_insertsAtBeginning() {
        // Arrange
        DiagnosisHistory oldHistory = DiagnosisHistory.builder()
                ._id("1")
                .diagnosisDate(new Date())
                .deficiency("Viejo")
                .build();

        DiagnosisHistory newHistory = DiagnosisHistory.builder()
                ._id("2")
                .diagnosisDate(new Date())
                .deficiency("Nuevo")
                .build();

        viewModel.addNewHistory(oldHistory);

        // Act
        viewModel.addNewHistory(newHistory);

        // Assert
        assertEquals("Nuevo", viewModel.getHistory().getValue().get(0).getDeficiency());
    }


}

