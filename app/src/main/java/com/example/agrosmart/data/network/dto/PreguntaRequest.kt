package com.example.agrosmart.data.network.dto

import com.google.gson.annotations.SerializedName

data class PreguntaRequest(
    @SerializedName("request")
    val request: String
)
