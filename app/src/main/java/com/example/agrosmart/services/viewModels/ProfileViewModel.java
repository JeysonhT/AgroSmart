package com.example.agrosmart.services.viewModels;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.agrosmart.R;
import com.example.agrosmart.models.User;
import com.example.agrosmart.services.auth.GoogleAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>();

    private GoogleAuthService googleAuthService;

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
