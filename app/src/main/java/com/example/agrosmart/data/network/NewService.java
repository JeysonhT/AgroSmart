package com.example.agrosmart.data.network;

import com.example.agrosmart.core.utils.interfaces.NewsCallBack;
import com.example.agrosmart.domain.repository.NewsRepository;

public class NewService {

    private NewsRepository repository;

    public NewService(NewsRepository repository){
        this.repository = repository;
    }

    public void getNews(NewsCallBack callBack){
        repository.getNews(callBack);
    }
}
