package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.data.local.UserDetailsLocalService;
import com.example.agrosmart.data.network.UserDetailsService;
import com.example.agrosmart.domain.models.UserDetails;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;

public class UserDtlUseCase {
    private final UserDetailsService usService = new UserDetailsService();
    private final UserDetailsLocalService uslService = new UserDetailsLocalService();

    @Inject
    public UserDtlUseCase() {}

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
        List<String> soilTypes = details.get().getSoilTypes();
        return (soilTypes != null && !soilTypes.isEmpty()) ? soilTypes.get(0) : null;
    }
}
