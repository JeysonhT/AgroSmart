package com.example.agrosmart.domain.models;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Entity // define que es una entidad de room para guardar en la base de datos
public class User {
    @PrimaryKey
    private int uid;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "role")
    private String role;
    private Uri imageUser;

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
}
