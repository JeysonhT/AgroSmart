package com.example.agrosmart.data.network;

import com.example.agrosmart.core.utils.interfaces.NewsCallBack;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.repository.NewsRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewService {

    private NewsRepository repository;

    public NewService(NewsRepository repository){
        this.repository = repository;
    }

    public CompletableFuture<List<News>> getNews(){
        return repository.getNews();
    }
}
