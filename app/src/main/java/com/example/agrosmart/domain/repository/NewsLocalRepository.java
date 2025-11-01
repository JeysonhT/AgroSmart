package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.News;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NewsLocalRepository {
    CompletableFuture<List<News>> getLocalNews();
    CompletableFuture<Boolean> saveNewsOnLocal(News news);

    CompletableFuture<Boolean> deleteNewOnLocal(String _id);

}
