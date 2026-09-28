package com.example.agrosmart.data.local

import com.example.agrosmart.data.repository.impl.UserDtlLocalImpl
import com.example.agrosmart.domain.models.UserDetails
import com.example.agrosmart.domain.repository.UserDtlRepository
import java.util.concurrent.CompletableFuture

class UserDetailsLocalService {
    private val repository: UserDtlRepository = UserDtlLocalImpl()

    fun getLocalDetails(fbUserName: String?): CompletableFuture<UserDetails?>? {
        return repository.getUserDetails(fbUserName)
    }

    fun saveDetailsOnLocal(
        userDetails: UserDetails?,
        email: String?
    ) {
        repository.postUserDetails(
                userDetails,
                email
        )
    }
}
