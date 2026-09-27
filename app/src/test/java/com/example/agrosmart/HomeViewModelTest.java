
package com.example.agrosmart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(JUnit4.class)
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private CropsUseCase mockCropsUseCase;

    private HomeViewModel viewModel;

    @Captor
    private ArgumentCaptor<CropsCallback> cropsCallbackCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new HomeViewModel(mockCropsUseCase);
    }

    @Test
    public void testLoadCrops_Success() {
        // Given
        List<Crop> crops = new ArrayList<>();
        crops.add(new Crop("Maiz", "descripcion", "12", "tipo"));
        when(mockCropsUseCase.getCrops()).thenReturn(CompletableFuture.completedFuture(crops));

        // When
        viewModel.loadCrops();

        // Then
        assertNotNull(viewModel.getCrops().getValue());
        assertEquals(1, viewModel.getCrops().getValue().size());
        assertEquals("Maiz", viewModel.getCrops().getValue().get(0).getTitle());
    }

    @Test
    public void testLoadCrops_Error() {
        // Given
        Exception exception = new Exception("Error loading crops");
        when(mockCropsUseCase.getCrops()).thenReturn(CompletableFuture.failedFuture(exception));

        // When
        viewModel.loadCrops();

        // Then
        assertNull(viewModel.getCrops().getValue());
    }
}
