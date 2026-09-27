package com.example.agrosmart.domain.models;

import android.net.Uri;

import java.util.UUID;

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
    private Uri imageUser;
    private Long lastUpdate;

    public User(String _username, String _email, Uri _imageUser){
        this.username = _username;
        this.email = _email;
        this.imageUser = _imageUser;
    }

    public User(){
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Uri getImageUser() { return imageUser; }
    public void setImageUser(Uri imageUser) { this.imageUser = imageUser; }

    public Long getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Long lastUpdate) { this.lastUpdate = lastUpdate; }
}
