package com.example.agrosmart.data.network

import com.example.agrosmart.data.repository.impl.UserDtlimpl
import com.example.agrosmart.domain.models.UserDetails
import com.example.agrosmart.domain.repository.UserDtlRepository
import java.util.concurrent.CompletableFuture

class UserDetailsService {
    private val repository: UserDtlRepository = UserDtlimpl()

    fun getUserDetails(fbUserName: String?): CompletableFuture<UserDetails?>? {
        return repository.getUserDetails(fbUserName)
    }

    fun postUserDetails(
        userDetails: UserDetails?,
        email: String?
    ) {
        repository.postUserDetails(
                userDetails,
                email
        )
    }
}
