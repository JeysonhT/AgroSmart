package com.example.agrosmart.domain.designModels;

public class ListView {
    private int icono;
    private String texto;

    public ListView(int icono, String texto) {
        this.icono = icono;
        this.texto = texto;
    }

    public int getIcono() {
        return icono;
    }

    public String getTexto() {
        return texto;
    }
}
