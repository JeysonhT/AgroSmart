package com.example.agrosmart.data.network.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class FirebaseAuthService {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void firebaseAuthWithGoogle(String idToken, AuthResultListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onAuthSuccess(firebaseAuth.getCurrentUser());
                    } else {
                        listener.onAuthFailure(task.getException());
                    }
                });
    }
}
