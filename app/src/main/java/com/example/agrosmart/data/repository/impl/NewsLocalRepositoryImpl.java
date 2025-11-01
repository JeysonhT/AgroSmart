package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.repository.NewsLocalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.Sort;
import io.realm.exceptions.RealmFileException;

public class NewsLocalRepositoryImpl implements NewsLocalRepository {

    private static final String TAG = "NEWS_LOCAL_REPOSITORY_IMPL";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    @Override
    public CompletableFuture<List<News>> getLocalNews() {

        return CompletableFuture.supplyAsync(()-> {
            Realm realm;
            List<News> result;

            try{
                realm = Realm.getDefaultInstance();

                result = realm.copyFromRealm(realm.where(News.class).
                        limit(10L).
                        sort("publicationDate", Sort.DESCENDING).
                        findAll());

                realm.close();

                return result;

            } catch (RealmFileException | IndexOutOfBoundsException e){
                Log.e(TAG, "Error al obtener las noticias: " + e.getMessage());
                throw new RuntimeException("Error al obtener los datos: " + e.getMessage());
            }

        } , executor);
    }

    @Override
    public CompletableFuture<Boolean> saveNewsOnLocal(News news) {

        return CompletableFuture.supplyAsync(()->{
            Realm realm;
            try{
                realm = Realm.getDefaultInstance();

                news.set_id(UUID.randomUUID().toString());
                news.setLocal(true);

                realm.executeTransaction(r -> {
                    r.insert(news);
                });

                return true;

            } catch (RealmFileException | NullPointerException e){
                Log.e(TAG, String.format("Error al guardar los datos: %s", e.getMessage()));
                return false;
            }
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> deleteNewOnLocal(String _id) {
        return CompletableFuture.supplyAsync(() -> {
            Realm realm;
            try{
                realm = Realm.getDefaultInstance();

                realm.executeTransaction(r -> {
                    News news1 = r.where(News.class).equalTo("_id", _id).findFirst();
                    if(news1!=null){
                        news1.deleteFromRealm();
                    }
                });

                return true;
            } catch (RealmFileException | NullPointerException e){
                Log.e(TAG, String.format("Error al eliminar la noticia: %s", e.getMessage()));
                return false;
            }
        }, executor);
    }
}
