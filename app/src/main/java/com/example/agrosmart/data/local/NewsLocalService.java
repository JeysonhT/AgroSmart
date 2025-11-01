package com.example.agrosmart.data.local;

import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.repository.NewsLocalRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewsLocalService {
    private NewsLocalRepository repository;

    public NewsLocalService(NewsLocalRepository _repository){
        this.repository = _repository;
    }

    public CompletableFuture<List<News>> getLocalNews(){
        return repository.getLocalNews();
    }

    public CompletableFuture<Boolean> saveNewsOnLocal(News news){
        return  repository.saveNewsOnLocal(news);
    }

    public CompletableFuture<Boolean> deleteFromLocal(String _id){
        return repository.deleteNewOnLocal(_id);
    }
}
