package com.example.agrosmart.domain.usecase;

import android.util.Log;

import com.example.agrosmart.data.network.MMLStatsService;
import com.example.agrosmart.domain.models.MMLStats;

import java.util.concurrent.ExecutionException;

public class MMLStatsUseCase {
    private final String TAG = "MMLStats_USE_CASE";
    private final MMLStatsService service;

    public MMLStatsUseCase(MMLStatsService _service){
        this.service = _service;
    }

    public void saveStats(MMLStats mmlStats){
        try{
            service.saveStats(mmlStats).thenRun(() -> System.out.println("Ejecutanto envio de MMLStats"))
                            .get();

            Log.d(TAG, "Exito en la operación");
        } catch (NullPointerException e) {
            Log.e(TAG, String.format("Error: %s", e.getMessage()));
            throw new RuntimeException(e);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
