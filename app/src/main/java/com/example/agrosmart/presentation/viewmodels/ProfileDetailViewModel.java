package com.example.agrosmart.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.data.repository.impl.UserDtlimpl;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Collection;
import java.util.List;

import io.realm.RealmList;


public class ProfileDetailViewModel extends ViewModel {

    private final String TAG = "PROFILE_DETAIL_VIEWMODEL";

    private final MutableLiveData<UserDetails> userDetails = new MutableLiveData<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserDtlRepository udRepository = new UserDtlimpl();

    public void postDetails(UserDetails userDetails, String email){
        udRepository.postUserDetails(db, userDetails, email);
    }

    public LiveData<UserDetails> getUserDetailsLiveData(String username){
        try {
            RealmList<String> lista = new RealmList<>();
            udRepository.getUserDetails(db, username, details -> {
                if(!details.isEmpty()){
                    lista.addAll((Collection<? extends String>) details.get("soilTypes"));
                    userDetails.setValue(new UserDetails(
                            username,
                            (String) details.get("email"),
                            (String) details.get("phoneNumber"),
                            (String) details.get("municipality"),
                            lista,
                            (String) details.get("status"),
                            (String) details.get("role"))
                    );
                }
            });
        } catch (NullPointerException e){
            Log.println(Log.ERROR, TAG, "User not have details saved");
        }

        return userDetails;
    }
}
