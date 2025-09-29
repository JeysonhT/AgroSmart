package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public DiagnosisHistory getLastDiagnosis() {
        Realm realm;
        DiagnosisHistory lastDiagnosis = new DiagnosisHistory();

        try{
            realm = Realm.getDefaultInstance();

            lastDiagnosis = realm.copyFromRealm((realm.where(DiagnosisHistory.class)
                    .sort("diagnosisDate", Sort.DESCENDING)
                    .findFirst()));

            realm.close();

            //return lastDiagnosis;
        } catch (RealmFileException e){
            Log.w(TAG, "Error: " + e.getMessage());
        }

        return lastDiagnosis;
    }

    @Override
    public void saveDiagnosis(DiagnosisHistory history, DiagnosisHistoryCallback callback) {
        Realm realm ;
        List<DiagnosisHistory> lastDiagnosis = new ArrayList<>();

        try {
            realm =  Realm.getDefaultInstance();
            //depuración

            realm.executeTransaction( r -> {
                r.insert(history);
            });
            realm.close();
            lastDiagnosis.add(history);

            callback.onLoaded(lastDiagnosis);
        } catch (RealmFileException e){
            callback.onError(e);
            Log.v(TAG, "Error al guardar el objeto: " + e.getMessage());
        }
    }

    @Override
    public void updateDiagnosis(String _id, String param, String value) {
        Thread thread = new Thread(() -> {
            Realm realm = null;

            try{
                realm = Realm.getDefaultInstance();

                realm.executeTransaction(r -> {
                    DiagnosisHistory diagnosis = r.where(DiagnosisHistory.class).
                            equalTo("_id", _id).findFirst();

                    if(diagnosis!=null){
                        diagnosis.setRecommendation(value);
                        r.insertOrUpdate(diagnosis);
                    }
                });

            } catch (RealmFileException | NullPointerException e){
                Log.w(TAG, Objects.requireNonNull(e.getMessage()));
            }  finally {
                if (realm != null && !realm.isClosed()) {
                    realm.close();
                }
            }
        });

        thread.start();
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
