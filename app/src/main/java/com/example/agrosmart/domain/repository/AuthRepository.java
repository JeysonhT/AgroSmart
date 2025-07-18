package com.example.agrosmart.domain.repository;

import androidx.fragment.app.Fragment;
import com.example.agrosmart.data.network.auth.AuthResultListener;

import android.content.Intent;

//interfaz para abstraer los inicios de sesion
public interface AuthRepository {
    Intent loginWithGoogle(Fragment fragment);
    void processGoogleSignInResult(Fragment fragment, Intent data, AuthResultListener listener);
}
