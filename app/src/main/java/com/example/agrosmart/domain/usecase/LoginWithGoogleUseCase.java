package com.example.agrosmart.domain.usecase;

import android.content.Context;
import android.content.Intent;

import com.example.agrosmart.data.network.auth.AuthResultListener;
import com.example.agrosmart.domain.repository.AuthRepository;

public class LoginWithGoogleUseCase {
    private final AuthRepository authRepository;

    public LoginWithGoogleUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Intent execute(Context context) {
        return authRepository.loginWithGoogle(context);
    }

    public void processResult(Context context, Intent data, AuthResultListener listener) {
        authRepository.processGoogleSignInResult(context, data, listener);
    }
}
