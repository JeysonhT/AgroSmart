package com.example.agrosmart.domain.usecase;

import androidx.fragment.app.Fragment;
import com.example.agrosmart.data.network.auth.AuthResultListener;
import com.example.agrosmart.domain.repository.AuthRepository;

import android.content.Intent;

public class LoginWithGoogleUseCase {
    private final AuthRepository authRepository;

    public LoginWithGoogleUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Intent execute(Fragment fragment) {
        return authRepository.loginWithGoogle(fragment);
    }

    public void processResult(Fragment fragment, Intent data, AuthResultListener listener) {
        authRepository.processGoogleSignInResult(fragment, data, listener);
    }
}
