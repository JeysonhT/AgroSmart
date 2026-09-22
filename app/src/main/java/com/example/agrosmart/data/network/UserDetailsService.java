package com.example.agrosmart.data.network;

import com.example.agrosmart.data.repository.impl.UserDtlimpl;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;

import java.util.concurrent.CompletableFuture;

public class UserDetailsService {
    private final UserDtlRepository repository = new UserDtlimpl();

    public CompletableFuture<UserDetails> getUserDetails(String fbUserName){
        return repository.getUserDetails(fbUserName);
    }

    public void postUserDetails(UserDetails userDetails, String email){
        repository.postUserDetails(userDetails, email);
    }
}
