package com.example.agrosmart.core.utils.interfaces;

import com.example.agrosmart.domain.models.News;

import java.util.List;

public interface NewsCallBack {
    void onLoaded(List<News> news);
    void onError(Exception e);
}
