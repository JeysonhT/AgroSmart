package com.example.agrosmart.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.domain.models.Deficiency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DetectionFragmentViewModel extends ViewModel {
    private final MutableLiveData<FirebaseUser> userAuth = new MutableLiveData<>();

    private final MutableLiveData<List<Deficiency>> deficiencyList = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userAuth.setValue(firebaseUser);

        return userAuth;
    }

    //metodo para obtener la info de deficiencias pendiente

    public void refreshData(){
        getUser();
    }
}
