package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.core.utils.classes.ImageEncoder;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.repository.NewsRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class NewsRepositoryImpl implements NewsRepository {

    private final String TAG = "NEWS_REPOSITORY";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<List<News>> getNews() {
        List<News> newsList = new ArrayList<>();

        CollectionReference docRef  = db.collection("News");

        Query query = docRef.whereEqualTo("status", "published")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(10L);

        CompletableFuture<List<News>> future = new CompletableFuture<>();

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
                            ImageEncoder.decoderBase64(doc.getString("imageBytes")),
                            doc.get("author", String.class),
                            doc.get("title", String.class),
                            doc.get("description", String.class),
                            Objects.requireNonNull(dateFormated),
                            doc.get("content", String.class)
                            ));
                }

                future.complete(newsList);
            }
        }).addOnFailureListener(future::completeExceptionally);

        return future;
    }
}
