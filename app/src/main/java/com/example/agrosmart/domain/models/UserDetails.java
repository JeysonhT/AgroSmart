package com.example.agrosmart.domain.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
//extender de realm
public class UserDetails extends RealmObject{
    @PrimaryKey
    private String _id;
    private String username;
    private String phoneNumber;
    private String municipality;
    private RealmList<String> soilTypes;

    public UserDetails(String username, String phoneNumber, String municipality, RealmList<String> soilTypes) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes;
    }

    public UserDetails(){}

}
