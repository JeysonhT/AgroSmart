package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.core.utils.interfaces.OnUserDetailsLoaded;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class UserDtlLocalImpl implements UserDtlRepository {

    @Override
    public void postUserDetails(UserDetails userDetails, String email) {

    }

    @Override
    public void getUserDetails(String fBSusername, OnUserDetailsLoaded callback) {

    }

    @Override
    public CompletableFuture<UserDetails> getUserDetails(String fbUserName) {
        return null;
    }
}
