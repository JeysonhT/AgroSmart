package com.example.agrosmart.domain.models;

import android.net.Uri;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User {
    private String username;
    private String email;
    private Uri imageUser;
}
