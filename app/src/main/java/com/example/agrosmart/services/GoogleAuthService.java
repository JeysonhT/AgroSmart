package com.example.agrosmart.services;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.agrosmart.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.function.Consumer;

public class GoogleAuthService {
    private final GoogleSignInClient googleSignInClient;
    private Fragment fragment;
    private final ActivityResultLauncher<Intent> launcher;

    public GoogleAuthService(Fragment fragment,
                             Consumer<FirebaseUser> onSuccess,
                             Consumer<Exception> onError){
        this.fragment = fragment;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(fragment.getString(R.string.google_oAuth_client))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(fragment.requireActivity(), gso);

        launcher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener(fragment.requireActivity(), authTask -> {
                                    if (authTask.isSuccessful()) {
                                        onSuccess.accept(FirebaseAuth.getInstance().getCurrentUser());
                                    } else {
                                        System.out.println("error");
                                        onError.accept(authTask.getException());
                                    }
                                });
                    } catch (ApiException e) {
                        onError.accept(e);
                    }
                }
        );
    }

    public void starSignIn(){
        launcher.launch(googleSignInClient.getSignInIntent());
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }
}
