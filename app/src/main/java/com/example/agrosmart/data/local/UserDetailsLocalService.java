package com.example.agrosmart.data.local;

import com.example.agrosmart.data.repository.impl.UserDtlLocalImpl;
import com.example.agrosmart.domain.models.UserDetails;
import com.example.agrosmart.domain.repository.UserDtlRepository;

import java.util.concurrent.CompletableFuture;

public class UserDetailsLocalService {
    private final UserDtlRepository repository = new UserDtlLocalImpl();

    public CompletableFuture<UserDetails> getLocalDetails(String fbUserName){
        return repository.getUserDetails(fbUserName);
    }

    public void saveDetailsOnLocal(UserDetails userDetails, String email){
        repository.postUserDetails(userDetails, email);
    }
}
