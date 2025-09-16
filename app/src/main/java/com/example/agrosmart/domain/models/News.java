package com.example.agrosmart.domain.models;


import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class News extends RealmObject {
    @PrimaryKey
    private String _id;
    private int imageResource;
    private byte[] image;
    private String author;
    private String newsName;
    private String description;
    private String publicationDate;
    private String information;
    private Long lastUpdate;

    public News(int imageResource, String news_name, String descripcion, String fecha_publicacion, String informacion) {
        this.imageResource = imageResource;
        this.newsName = news_name;
        this.description = descripcion;
        this.publicationDate = fecha_publicacion;
        this.information = informacion;
    }

    public News(byte[] _image, String _author, String _newsName, String _description, String _date, String _information){
        this.image = _image;
        this.author = _author;
        this.newsName = _newsName;
        this.description = _description;
        this.publicationDate = _date;
        this.information = _information;
    }

    public News(){
        this._id = UUID.randomUUID().toString();
    }
}
