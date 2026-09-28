package com.example.agrosmart.data.network.dto

import com.google.gson.annotations.SerializedName
import lombok.Getter
import lombok.Setter


@Getter
@Setter
class PreguntaRequest(@field:SerializedName("request") private val request: String?)
