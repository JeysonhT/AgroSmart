package com.example.agrosmart.designModels;

import android.graphics.drawable.Drawable;

public class ListViewModel {
    private int icono;
    private String texto;

    public ListViewModel(int icono, String texto) {
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
