package com.example.agrosmart.domain.repository

import com.example.agrosmart.core.utils.interfaces.OnUserDetailsLoaded
import com.example.agrosmart.domain.models.UserDetails
import java.util.concurrent.CompletableFuture

interface UserDtlRepository {
    fun postUserDetails(
        userDetails: UserDetails?,
        email: String?
    )

    fun getUserDetails(
        fBSusername: String?,
        callback: OnUserDetailsLoaded?
    )

    fun getUserDetails(fbUserName: String?): CompletableFuture<UserDetails?>?
}
