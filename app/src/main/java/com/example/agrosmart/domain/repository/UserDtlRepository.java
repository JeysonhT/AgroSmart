package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.core.utils.interfaces.OnUserDetailsLoaded;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public interface UserDtlRepository {
    void postUserDetails(UserDetails userDetails, String email);
    void getUserDetails(String fBSusername, OnUserDetailsLoaded callback);

    CompletableFuture<UserDetails> getUserDetails(String fbUserName);
}
