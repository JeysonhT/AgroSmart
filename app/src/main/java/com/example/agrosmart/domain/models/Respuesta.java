package com.example.agrosmart.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Respuesta {
    private String respuesta;

    public Respuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
