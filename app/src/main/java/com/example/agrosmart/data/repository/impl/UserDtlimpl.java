package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;
import com.example.agrosmart.core.utils.interfaces.OnUserDetailsLoaded;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDtlimpl implements UserDtlRepository {

    private final String TAG = "AgroSmartFirestore";
    @Override
    public void postUserDetails(FirebaseFirestore db, UserDetails userDetails) {
        // se guarda el documento y se le proporciona como identificador el Email de google
        db.collection("userDetails").document(userDetails.getUsername())
                .set(userDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "documento guardado");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "No se puede guardar el documento");
                    }
                });
    }

    @Override
    public void getUserDetails(FirebaseFirestore db, String fBSusername, OnUserDetailsLoaded callback) {
        Map<String, Object> detail = new HashMap<>();

        // se declara la referencia de el documento de id fBSusername, que es el email de Google
        DocumentReference dodRef = db.collection("userDetails").document(fBSusername);

        // se procede a realizar la tarea de buscar el documento, si existe se edita, si no se crea
        dodRef.get().addOnCompleteListener(task -> {
            Map<String, Object> details = new HashMap<>();
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "documento encontrado: " + document.getData());
                    /* se llenan los campos del mapa details para ser usado en rellenar
                       los campos del formulario de editar perfil
                    * */
                    details.put("username", document.get("username", String.class));
                    details.put("phoneNumber", document.get("phoneNumber", String.class));
                    details.put("municipality", document.get("municipality", String.class));
                    details.put("soilTypes", document.get("soilTypes"));

                } else {
                    Log.d(TAG, "Documento no encontrado");
                }
            } else {
                Log.d("error de conexión", String.valueOf(task.getException()));
            }
            // este callback esperara los datos antes de mandar el resultado
            callback.onLoaded(details);
        });
    }
}
