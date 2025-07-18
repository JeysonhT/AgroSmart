package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.core.utils.interfaces.OnUserDetailsLoaded;
import com.google.firebase.firestore.FirebaseFirestore;

public interface UserDtlRepository {
    void postUserDetails(FirebaseFirestore db, UserDetails userDetails);
    void getUserDetails(FirebaseFirestore db, String fBSusername, OnUserDetailsLoaded callback);
}
