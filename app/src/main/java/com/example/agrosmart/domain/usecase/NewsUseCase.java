package com.example.agrosmart.domain.usecase;

import android.content.Context;

import com.example.agrosmart.core.utils.classes.NetworkChecker;
import com.example.agrosmart.core.utils.interfaces.NewsCallBack;
import com.example.agrosmart.data.local.NewsLocalService;
import com.example.agrosmart.data.network.NewService;
import com.example.agrosmart.data.repository.impl.NewsLocalRepositoryImpl;
import com.example.agrosmart.data.repository.impl.NewsRepositoryImpl;
import com.example.agrosmart.domain.models.News;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewsUseCase {
    private final NewService newService;
    private final NewsLocalService newsLocalService;

    public NewsUseCase(){
        this.newService = new NewService(new NewsRepositoryImpl());
        this.newsLocalService = new NewsLocalService(new NewsLocalRepositoryImpl());
    }

    public CompletableFuture<List<News>> getNewsUseCase(Context context){

        if(NetworkChecker.isInternetAvailable(context)){
            return newService.getNews();
        } else {
            return newsLocalService.getLocalNews();
        }

    }

    public CompletableFuture<Boolean> saveNewOnLocal(News news){
        return newsLocalService.saveNewsOnLocal(news);
    }

    public CompletableFuture<Boolean> deleteFromLocal(String _id){
        return newsLocalService.deleteFromLocal(_id);
    }

    public CompletableFuture<List<News>> getLocalNews(){
        return newsLocalService.getLocalNews();
    }
}
