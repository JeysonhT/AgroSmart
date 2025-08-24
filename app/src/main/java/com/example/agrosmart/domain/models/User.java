package com.example.agrosmart.domain.models;

import android.net.Uri;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
 // define que es una entidad de realm para guardar en la base de datos
public class User {
    //@PrimaryKey
    private String _id;
    private String username;
    private String email;
    private String role;
    private Uri imageUser;
    private Long lastUpdate;

    public User(String _username, String _email, Uri _imageUser){
        this.username = _username;
        this.email = _email;
        this.imageUser = _imageUser;
    }

    public User(String _username, String _email, String _role){
        this.username = _username;
        this.email = _email;
        this.role = _role;
    }

    public User(){
        this._id = UUID.randomUUID().toString();
    }
}
