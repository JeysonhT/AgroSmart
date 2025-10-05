package com.example.agrosmart;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class RecommendationUnitTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private GetRecommendationUseCase mockRecommendationUseCase;

    private DetectionFragmentViewModel viewModel;

    @Before
    public void setup(){
        mockRecommendationUseCase = mock(GetRecommendationUseCase.class);

        viewModel = new DetectionFragmentViewModel(mockRecommendationUseCase);
    }

    @Test
    public void obtainRecommendation(){
        //configuración
        Respuesta fakeResponse = new Respuesta("Recommendation Test");

        when(mockRecommendationUseCase.ejecutar(anyString()))
                .thenReturn(
                        CompletableFuture.completedFuture(fakeResponse)
                );
        Observer<Respuesta> observer = mock(Observer.class);
        viewModel.getRecommendationResponse().observeForever(observer);

        //acción
        viewModel.obtenerRecomendacion("Problema en el frijol");

        //evaluación
        verify(observer).onChanged(fakeResponse);
    }
}
