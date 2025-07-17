package com.example.agrosmart.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.domain.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<User> getUserData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser !=null){
            User userAuth = new User(
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail(),
                    firebaseUser.getPhotoUrl()
            );
            user.setValue(userAuth);
        } else {
            user.setValue(null);
        }

        return user;
    }

    public void refreshData(){
        getUserData();
    }

}
