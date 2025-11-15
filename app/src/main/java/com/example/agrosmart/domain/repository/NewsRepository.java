package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.News;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NewsRepository {
    CompletableFuture<List<News>> getNews();
}
