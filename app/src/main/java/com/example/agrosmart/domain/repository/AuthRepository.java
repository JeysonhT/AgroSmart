package com.example.agrosmart.domain.repository;

import android.content.Context;
import android.content.Intent;
import com.example.agrosmart.data.network.auth.AuthResultListener;

// Interfaz para abstraer los inicios de sesion
public interface AuthRepository {
    Intent loginWithGoogle(Context context);
    void processGoogleSignInResult(Context context, Intent data, AuthResultListener listener);
}
