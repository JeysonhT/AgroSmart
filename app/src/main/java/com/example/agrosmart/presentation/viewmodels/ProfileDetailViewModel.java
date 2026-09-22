package com.example.agrosmart.presentation.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.core.utils.classes.NetworkChecker;
import com.example.agrosmart.data.repository.impl.UserDtlimpl;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;
import com.example.agrosmart.domain.usecase.UserDtlUseCase;


import java.util.Collection;
import java.util.Objects;

import io.realm.RealmList;


public class ProfileDetailViewModel extends ViewModel {

    private final String TAG = "PROFILE_DETAIL_VIEWMODEL";

    private final MutableLiveData<UserDetails> userDetails = new MutableLiveData<>();

    private final UserDtlRepository udRepository = new UserDtlimpl();

    private final UserDtlUseCase useCase = new UserDtlUseCase();

    public void postDetails(UserDetails userDetails, String email){
        udRepository.postUserDetails(userDetails, email);
    }

    public LiveData<UserDetails> getUserDetailsLiveData(String username, Context context){

        if(NetworkChecker.isInternetAvailable(context)){
            useCase.getUserDetails(username)
                            .thenAccept(userDetails::postValue)
                    .exceptionally(ex -> {
                        Log.e(TAG, Objects.requireNonNull(ex.getMessage()));
                        userDetails.postValue(null);
                        return null;
                    });

        } else {
            useCase.getLocalDetails(username)
                    .thenAccept(userDetails::postValue)
                    .exceptionally(ex -> {
                        Log.e(TAG, Objects.requireNonNull(ex.getMessage()));
                        userDetails.postValue(null);
                        return null;
                    });
        }

        return userDetails;
    }
}
