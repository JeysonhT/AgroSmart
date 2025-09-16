package com.example.agrosmart.data.network.dto;



import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PreguntaRequest {
    @SerializedName("request")
    private String request;

    public PreguntaRequest(String request) {
        this.request = request;
    }
}
