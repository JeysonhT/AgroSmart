package com.example.agrosmart.data.repository.impl;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import com.example.agrosmart.data.network.auth.AuthResultListener;
import com.example.agrosmart.data.network.auth.GoogleAuthService;
import com.example.agrosmart.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public Intent loginWithGoogle(Fragment fragment) {
        return new GoogleAuthService(fragment).starSignIn();
    }

    public void processGoogleSignInResult(Fragment fragment, Intent data, AuthResultListener listener) {
        new GoogleAuthService(fragment).handleSignInResult(data, listener);
    }
}
