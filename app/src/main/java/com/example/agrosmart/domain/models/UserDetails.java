package com.example.agrosmart.domain.models;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserDetails {
    private String username;
    private String phoneNumber;
    private String municipality;
    private List<String> soilTypes;

    public UserDetails(String username, String phoneNumber, String municipality, List<String> soilTypes) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes;
    }

}
