package com.example.agrosmart.data.repository.impl;

import android.content.Context;
import android.content.Intent;

import com.example.agrosmart.data.network.auth.AuthResultListener;
import com.example.agrosmart.data.network.auth.GoogleAuthService;
import com.example.agrosmart.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    @Override
    public Intent loginWithGoogle(Context context) {
        return new GoogleAuthService(context).starSignIn();
    }

    @Override
    public void processGoogleSignInResult(Context context, Intent data, AuthResultListener listener) {
        new GoogleAuthService(context).handleSignInResult(data, listener);
    }
}
