package com.example.agrosmart.domain.repository;

import com.example.agrosmart.core.utils.interfaces.NewsCallBack;

public interface NewsRepository {
    void getNews(NewsCallBack callBack);
}
