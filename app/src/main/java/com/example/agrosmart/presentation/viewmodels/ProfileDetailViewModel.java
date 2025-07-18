package com.example.agrosmart.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.data.repository.impl.UserDtlimpl;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;


public class ProfileDetailViewModel extends ViewModel {

    private final MutableLiveData<UserDetails> userDetails = new MutableLiveData<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserDtlRepository udRepository = new UserDtlimpl();

    public void postDetails(UserDetails userDetails){
        udRepository.postUserDetails(db, userDetails);
    }

    public LiveData<UserDetails> getUserDetailsLiveData(String username){
        udRepository.getUserDetails(db, username, details -> {
            userDetails.setValue(new UserDetails(
                    username,
                    (String) details.get("phoneNumber"),
                    (String) details.get("municipality"),
                    (List<String>) details.get("soilTypes")
            ));

        });

        return userDetails;
    }
}
