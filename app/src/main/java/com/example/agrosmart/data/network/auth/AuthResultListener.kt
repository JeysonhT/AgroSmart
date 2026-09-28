package com.example.agrosmart.data.network.auth

import com.google.firebase.auth.FirebaseUser

interface AuthResultListener {
    fun onAuthSuccess(user: FirebaseUser?)
    fun onAuthFailure(e: Exception?)
}
