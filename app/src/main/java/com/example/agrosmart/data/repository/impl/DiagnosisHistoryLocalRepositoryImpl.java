package com.example.agrosmart.data.repository.impl;

import android.util.Log;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmFileException;

public class DiagnosisHistoryLocalRepositoryImpl implements DiagnosisHistoryRepository {
    private final String TAG = "DIAGNOSIS_HISTORY_REPOSITORY";

    @Override
    public List<DiagnosisHistory> getDiagnosisHistories() {
        Realm realm;
        List<DiagnosisHistory> historyList = new ArrayList<>();
        //deleteEmptyDiagnosis();
        try{
            realm = Realm.getDefaultInstance();
            historyList = realm.copyFromRealm(realm.where(DiagnosisHistory.class)
                            .sort("diagnosisDate", Sort.DESCENDING)
                    .limit(10L).findAll());
            realm.close();
        } catch (RealmFileException e) {
            Log.v(TAG, "Error al obtener el historial: " + e.getMessage());
        }
        return historyList;
    }

    @Override
    public void saveDiagnosis(DiagnosisHistory history) {
        Realm realm ;

        try {
            realm =  Realm.getDefaultInstance();
            //depuración

            realm.executeTransaction( r -> {
                r.insert(history);
            });
            realm.close();
        } catch (RealmFileException e){
            Log.v(TAG, "Error al guardar el objeto: " + e.getMessage());
        }
    }

    @Override
    public void deleteEmptyDiagnosis() {
        Realm realm;
        try{
            realm = Realm.getDefaultInstance();

            realm.executeTransaction( r -> {
                RealmResults<DiagnosisHistory> histories = r.where(DiagnosisHistory.class)
                                .findAll();
                histories.deleteAllFromRealm();
            });

            realm.close();
        } catch (RealmFileException e){
            Log.v(TAG, "Error al abrir la instancia de realm");
        }
    }

    @Override
    public void deleteDiagnosis(String _id) {
        Thread thread = new Thread(() -> {
            Realm realm;

            try{
                realm = Realm.getDefaultInstance();

                realm.executeTransaction(r -> {
                    DiagnosisHistory h = realm.where(DiagnosisHistory.class)
                            .equalTo("_id", _id).findFirst();

                    if(h!=null){
                        h.deleteFromRealm();
                    }
                    h = null;
                });

                realm.close();
            } catch(RealmFileException e){
                Log.v(TAG, "Error al eliminar el objeto: " + e.getMessage());
            }
        });

        thread.start();
    }
}
