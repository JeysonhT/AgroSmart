package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.data.local.UserDetailsLocalService;
import com.example.agrosmart.data.network.UserDetailsService;
import com.example.agrosmart.domain.models.UserDetails;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserDtlUseCase {
    private final UserDetailsService usService = new UserDetailsService();
    private final UserDetailsLocalService uslService = new UserDetailsLocalService();

    public CompletableFuture<UserDetails> getUserDetails(String fbUserName){
        return usService.getUserDetails(fbUserName);
    }

    public CompletableFuture<UserDetails> getLocalDetails(String fbUserName){
        return uslService.getLocalDetails(fbUserName);
    }

    public void postUseDetails(UserDetails details, String email){
        usService.postUserDetails(details, email);
    }

    public void saveDetailsOnLocal(UserDetails details, String email){
        uslService.saveDetailsOnLocal(details, email);
    }

    public String getSoilTypeFromDetail(String email) throws ExecutionException, InterruptedException {
        CompletableFuture<UserDetails> details = usService.getUserDetails(email);

        return details.get().getSoilTypes().first();
    }
}
