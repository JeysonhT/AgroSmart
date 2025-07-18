package com.example.agrosmart.data.network.auth;

import com.google.firebase.auth.FirebaseUser;

public interface AuthResultListener {
    void onAuthSuccess(FirebaseUser user);
    void onAuthFailure(Exception e);
}
