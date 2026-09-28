package com.example.agrosmart.data.network.dto

data class UserDetailsDto(
    val username: String,
    val phoneNumber: String,
    val email: String,
    val municipality: String,
    val soilTypes: MutableList<String?>,
    val role: String,
    val status: String
)
