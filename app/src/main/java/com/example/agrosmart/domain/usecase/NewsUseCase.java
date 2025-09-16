package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.core.utils.interfaces.NewsCallBack;
import com.example.agrosmart.data.network.NewService;
import com.example.agrosmart.data.repository.impl.NewsRepositoryImpl;

public class NewsUseCase {
    private final NewService newService;

    public NewsUseCase(){
        this.newService = new NewService(new NewsRepositoryImpl());
    }

    public void getNewsUseCase(NewsCallBack callBack){
        newService.getNews(callBack);
    }
}
