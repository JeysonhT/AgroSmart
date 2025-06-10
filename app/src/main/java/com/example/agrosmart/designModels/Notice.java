package com.example.agrosmart.designModels;

public class Notice {
    private int imageResource;
    private String nombre_noticia;
    private String descripcion;
    private String fecha_publicacion;
    private String informacion;

    public Notice(int imageResource, String nombre_noticia, String descripcion, String fecha_publicacion, String informacion) {
        this.imageResource = imageResource;
        this.nombre_noticia = nombre_noticia;
        this.descripcion = descripcion;
        this.fecha_publicacion = fecha_publicacion;
        this.informacion = informacion;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getNombre_noticia() {
        return nombre_noticia;
    }

    public void setNombre_noticia(String nombre_noticia) {
        this.nombre_noticia = nombre_noticia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}
