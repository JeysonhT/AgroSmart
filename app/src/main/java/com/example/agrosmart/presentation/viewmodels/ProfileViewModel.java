package com.example.agrosmart.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.data.repository.impl.UserDtlimpl;
import com.example.agrosmart.domain.models.User;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

import io.realm.RealmList;

public class ProfileViewModel extends ViewModel {

    private final String TAG = "PROFILE_VIEW_MODEL";

    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<UserDetails> userDtl = new MutableLiveData<>();

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

    public LiveData<UserDetails> getUserDetails(String username){
        UserDtlRepository repository = new UserDtlimpl();

        try {
            RealmList<String> lista = new RealmList<>();
            repository.getUserDetails(FirebaseFirestore.getInstance(), username, details -> {
                if(!details.isEmpty()){
                    lista.addAll((Collection<? extends String>) details.get("soilTypes"));
                    userDtl.setValue(new UserDetails(
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

        return userDtl;

    }

}
