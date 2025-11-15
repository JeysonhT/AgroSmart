package com.example.agrosmart.domain.usecase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.agrosmart.data.local.NewsLocalService;
import com.example.agrosmart.data.network.NewService;
import com.example.agrosmart.domain.models.News;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(MockitoJUnitRunner.class)
public class NewsUseCaseTest {

    @Mock
    private NewService mockNewService;

    @Mock
    private NewsLocalService mockNewsLocalService;

    private NewsUseCase newsUseCase;

    private News dummyNews;

    @Before
    public void setUp() {
        newsUseCase = new NewsUseCase(mockNewService, mockNewsLocalService);
        dummyNews = new News(new byte[0], "author", "title", "desc", "date", "content");
    }

    // --- Tests for saveNewOnLocal ---

    @Test
    public void saveNewOnLocal_shouldReturnTrueOnSuccess() throws ExecutionException, InterruptedException {
        // Arrange
        when(mockNewsLocalService.saveNewsOnLocal(any(News.class)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Act
        CompletableFuture<Boolean> future = newsUseCase.saveNewOnLocal(dummyNews);

        // Assert
        assertTrue(future.get());
        verify(mockNewsLocalService).saveNewsOnLocal(dummyNews);
    }

    @Test
    public void saveNewOnLocal_shouldReturnFalseOnFailure() throws ExecutionException, InterruptedException {
        // Arrange
        when(mockNewsLocalService.saveNewsOnLocal(any(News.class)))
                .thenReturn(CompletableFuture.completedFuture(false));

        // Act
        CompletableFuture<Boolean> future = newsUseCase.saveNewOnLocal(dummyNews);

        // Assert
        assertFalse(future.get());
        verify(mockNewsLocalService).saveNewsOnLocal(dummyNews);
    }

    // --- Tests for getLocalNews ---

    @Test
    public void getLocalNews_shouldReturnNewsListOnSuccess() throws ExecutionException, InterruptedException {
        // Arrange
        List<News> newsList = Arrays.asList(dummyNews, dummyNews);
        when(mockNewsLocalService.getLocalNews())
                .thenReturn(CompletableFuture.completedFuture(newsList));

        // Act
        CompletableFuture<List<News>> future = newsUseCase.getLocalNews();

        // Assert
        List<News> result = future.get();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(newsList, result);
        verify(mockNewsLocalService).getLocalNews();
    }

    @Test
    public void getLocalNews_shouldReturnEmptyListWhenNoNews() throws ExecutionException, InterruptedException {
        // Arrange
        when(mockNewsLocalService.getLocalNews())
                .thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));

        // Act
        CompletableFuture<List<News>> future = newsUseCase.getLocalNews();

        // Assert
        List<News> result = future.get();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockNewsLocalService).getLocalNews();
    }

    @Test(expected = ExecutionException.class)
    public void getLocalNews_shouldPropagateExceptionOnFailure() throws ExecutionException, InterruptedException {
        // Arrange
        RuntimeException exception = new RuntimeException("Database error");
        CompletableFuture<List<News>> exceptionallyFuture = new CompletableFuture<>();
        exceptionallyFuture.completeExceptionally(exception);

        when(mockNewsLocalService.getLocalNews()).thenReturn(exceptionallyFuture);

        // Act
        newsUseCase.getLocalNews().get(); // This should throw ExecutionException
    }
}
