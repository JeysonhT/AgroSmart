package com.example.agrosmart.data.repository.impl;

import android.util.Base64;
import android.util.Log;

import com.example.agrosmart.core.utils.interfaces.NewsCallBack;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.repository.NewsRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewsRepositoryImpl implements NewsRepository {

    private final String TAG = "NEWS_REPOSITORY";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getNews(NewsCallBack callBack) {
        List<News> newsList = new ArrayList<>();

        CollectionReference docRef  = db.collection("News");

        Query query = docRef.orderBy("date", Query.Direction.DESCENDING)
                .limit(10L);

        query.get().addOnCompleteListener( querySnapshotTask -> {
            if(querySnapshotTask.isSuccessful()){
                List<DocumentSnapshot> documents = querySnapshotTask.getResult().getDocuments();
                Timestamp timestamp;

                for(DocumentSnapshot doc : documents){
                    timestamp = new Timestamp(((Timestamp) doc.get("date")).toDate());

                    Date  date = timestamp.toDate();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String dateFormated = format.format(date);

                    newsList.add(new News(
                            base64ToImage(doc.getString("imageBytes")),
                            doc.get("author", String.class),
                            doc.get("title", String.class),
                            doc.get("description", String.class),
                            Objects.requireNonNull(dateFormated),
                            doc.get("content", String.class)
                            ));
                }

                callBack.onLoaded(newsList);
            }
        }).addOnFailureListener(callBack::onError);
    }

    private byte[] base64ToImage(String base64String){

        try {
            if(base64String.contains(",")){
                base64String = base64String.split(",")[1];
            }

            return Base64.decode(base64String, Base64.DEFAULT);

        } catch (Exception e){
            Log.println(Log.ERROR, TAG, "Error to convert image");
            return null;
        }


    }
}
