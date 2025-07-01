package com.example.agrosmart.services.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.models.UserDetails;
import com.example.agrosmart.services.repository.UserDetailsRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ProfileDetailViewModel extends ViewModel {

    private MutableLiveData<UserDetails> userDetails = new MutableLiveData<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserDetailsRepository udRepository = new UserDetailsRepository();

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
