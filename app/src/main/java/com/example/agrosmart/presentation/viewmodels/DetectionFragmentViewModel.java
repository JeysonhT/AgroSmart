package com.example.agrosmart.presentation.viewmodels;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.data.repository.impl.RecommendationServiceImpl;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase;
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectionFragmentViewModel extends ViewModel {

    private final String TAG = "DETECTION_FRAGMENT_VIEWMODDEL";

    private final MutableLiveData<List<DiagnosisHistoryListView>> histories = new MutableLiveData<>();
    private final MutableLiveData<DiagnosisHistoryListView> lastDiagnosis = new MutableLiveData<>();

    //metodos y variables para la creacion y devolucion de recomendaciones
    private final MutableLiveData<Respuesta> recommendationResponse = new MutableLiveData<>();

    private final GetRecommendationUseCase usecase;

    private final DiagnosisHistoryUseCase diagnosisUseCase;

    public DetectionFragmentViewModel() {
        this.usecase = new GetRecommendationUseCase(new RecommendationServiceImpl());
        this.diagnosisUseCase = new DiagnosisHistoryUseCase();
    }

    public LiveData<Respuesta> getRecommendationResponse(){
        return recommendationResponse;
    }

    public LiveData<DiagnosisHistoryListView> getLastDiagnosis() {
        return lastDiagnosis; }

    //metodo que consume el caso de uso de generacion de detecciones
    public void obtenerRecomendacion(String problema){

        final String pregunta = "Comportate como un agronomo profesional y " +
                "genera recomendaciones para el siguiente problema\n" +
                problema + "\n" +
                "puedes recomendar fertilizantes organicos y no organicos, " +
                "no excedas las 300 palabras";

        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                usecase.ejecutar(pregunta, new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                        if(response.isSuccessful() && response.body() != null){
                            handler.post(()->recommendationResponse.postValue(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Respuesta> call, Throwable t) {
                        handler.post(()-> recommendationResponse.postValue(new Respuesta("error: " + t.getMessage())));
                    }
                });
            }
        });

        thread.start();
    }

    public void saveDiagnosis(String diagnosis, byte[] image, Consumer<DiagnosisHistoryListView> onSave){
        CropsUseCase cropsCase = new CropsUseCase();

        String name = diagnosis.split(" ")[0];
        final Crop[] crop = new Crop[1];
        Handler handler = new Handler(Looper.getMainLooper());
        cropsCase.getCropByName(name, new CropsCallback() {
            @Override
            public void onCropsLoaded(List<Crop> crops) {
                Thread thread = new Thread(() -> {
                    crop[0] = crops.get(0);
                    DiagnosisHistory diagnosisHistory = DiagnosisHistory.builder()
                                ._id(UUID.randomUUID().toString())
                                .diagnosisDate(new Date())
                                .Crop(crop[0])
                                .deficiency(diagnosis)
                                .image(image)
                                .recommendation("")
                                .lastUpdate(System.currentTimeMillis())
                                .build();
                    //pasar el valor al fragment detection

                    handler.post(() -> {
                        onSave.accept(DiagnosisToHistoryLV(diagnosisHistory));
                        lastDiagnosis.postValue(DiagnosisToHistoryLV(diagnosisHistory));
                    });

                    diagnosisUseCase.saveDiagnosis(diagnosisHistory, new DiagnosisHistoryCallback() {
                        @Override
                        public void onLoaded(List<DiagnosisHistory> history) {
                            handler.post(() -> {
                                System.out.println("valor obtenido: " + history.get(0).getDiagnosisDate());
                                //lastDiagnosis.postValue(DiagnosisToHistoryLV(history.get(0)));
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.println(Log.ERROR, TAG, "Error al obtener el ultimo diagnostico" +
                                        "/n : " + e.getMessage());
                        }
                    });

                    Log.println(Log.DEBUG, TAG, "Valor guardado: " + diagnosisHistory.getDiagnosisDate());
                });

                thread.start();
            }
            @Override
            public void onError(Exception e) {
                Log.println(Log.ERROR, TAG, Objects.requireNonNull(e.getMessage()));
            }
        });

    }

    //metodo para obtener la info de deficiencias pendiente
    public LiveData<List<DiagnosisHistoryListView>> getHistory(){
        return histories;
    }

    public void gethistoriesFromUseCase(){
        final List<DiagnosisHistoryListView> diagnosisHistoryItems = new ArrayList<>();
        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(() -> {
            diagnosisUseCase.getHistories(historyList -> {
                if(!historyList.isEmpty()){
                    try{
                        for (DiagnosisHistory dh: historyList ) {
                            diagnosisHistoryItems.add(DiagnosisToHistoryLV(dh));
                        }
                        handler.post(() -> {
                            histories.postValue(diagnosisHistoryItems);
                            lastDiagnosis.postValue(diagnosisHistoryItems.get(0));
                        });
                    } catch(NullPointerException e){
                        Log.println(Log.ERROR, TAG, Objects.requireNonNull(e.getMessage()));
                    }
                }
            });
        });

        thread.start();
    }

    public void addNewHistory(DiagnosisHistoryListView newHistory) {
        List<DiagnosisHistoryListView> currentList = histories.getValue();
        List<DiagnosisHistoryListView> newList = new ArrayList<>();

        if (currentList != null) {
            newList.addAll(currentList);
        }
        newList.add(0, newHistory); // Agregar al inicio

        histories.setValue(newList);
    }

    public void deleteHistory(String _id){
        diagnosisUseCase.deleteDiagnosis(_id);
    }

    public void updateDiagnosis(String _id, String value){
        diagnosisUseCase.updateDiagnosis(_id, value);
    }

    private int[] setHistoryIcons(String nameCrop, String nameDeficiency){
        int[] resources = new int[2];
        //primero evaluamos el nombre del cultivo
        switch (nameCrop){
            case "Maíz":
                resources[0] = R.drawable.maiz;
                break;
            case "Frijol":
                resources[0] = R.drawable.frijoles_rojos;
                break;
            default:
                resources[0] = R.drawable.wheat;
                break;
        }

        //luego evaluamos el nombre de la deficiencias -- primero obteniendo la ultima palabra del resultado
        int lastSpace = nameDeficiency.lastIndexOf(" ");
        String deficiency = (lastSpace == -1) ? nameDeficiency : nameDeficiency.substring(lastSpace + 1);

        switch (deficiency){
            case "nitrogeno":
                resources[1] = R.drawable.nitrogeno;
                break;
            case "fosforo":
                resources[1] = R.drawable.fosforo;
                break;
            case "magnesio":
                resources[1] = R.drawable.magnesio;
                break;
            case "potasio":
                resources[1] = R.drawable.potasio;
                break;
            default:
                resources[1] = R.drawable.cultivo_sano;
                break;
        }

        return resources;
    }

    private DiagnosisHistoryListView DiagnosisToHistoryLV(DiagnosisHistory dh){
        int [] resources = new int[2];
        resources = setHistoryIcons(dh.getCrop().getCropName(), dh.getDeficiency());
        Date date = dh.getDiagnosisDate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateFormated = format.format(date);

        DiagnosisHistoryListView history = new DiagnosisHistoryListView();

        history.setCropIcon(resources[0]);
        history.setDeficiencyIcon(resources[1]);
        history.setId(dh.get_id());
        history.setTxtDate(dateFormated);
        history.setDeficiency(dh.getDeficiency());
        history.setImage(dh.getImage());
        history.setRecommendation(dh.getRecommendation());

        return history;
    }

    //refresca la información del usuario
    public void refreshData(){
        gethistoriesFromUseCase();
    }

    public void cleanRecommendation(){
        recommendationResponse.postValue(null);
    }
}
