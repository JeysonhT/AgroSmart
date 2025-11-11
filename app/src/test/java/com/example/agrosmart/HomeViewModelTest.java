
package com.example.agrosmart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.NewsUseCase;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private CropsUseCase mockCropsUseCase;

    @Mock
    private NewsUseCase mockNewsUseCase;

    @Mock
    Context mockContext;

    private HomeViewModel viewModel;

    @Captor
    private ArgumentCaptor<CropsCallback> cropsCallbackCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new HomeViewModel(mockNewsUseCase, mockCropsUseCase);
        when(mockContext.getString(anyInt())).thenReturn("Mocked");
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

    @Test
    public void testLoadNews(){
        List<News> newsList = new ArrayList<>();

        newsList.add(
                new News(new byte[0], "texto de prueba")
        );

        when(mockNewsUseCase.getNewsUseCase(mockContext)).thenReturn(CompletableFuture.completedFuture(newsList));

        viewModel.loadNews(mockContext);

        assertNotNull(viewModel.getNews().getValue());
        assertEquals(1, viewModel.getNews().getValue().size());
        assertEquals("texto de prueba", viewModel.getNews().getValue().get(0).getDescription());
    }

    @Test
    public void testLoadNews_error(){
        // Given
        Exception exception = new Exception("Error loading news");
        when(mockNewsUseCase.getNewsUseCase(mockContext)).thenReturn(CompletableFuture.failedFuture(exception));

        // When
        viewModel.loadNews(mockContext);

        // Then
        assertNull(viewModel.getNews().getValue());
    }
}
