package com.example.agrosmart.domain.repository

import android.content.Context
import android.content.Intent
import com.example.agrosmart.data.network.auth.AuthResultListener

// Interfaz para abstraer los inicios de sesion
interface AuthRepository {
    fun loginWithGoogle(context: Context?): Intent?
    fun processGoogleSignInResult(
        context: Context?,
        data: Intent?,
        listener: AuthResultListener?
    )
}
