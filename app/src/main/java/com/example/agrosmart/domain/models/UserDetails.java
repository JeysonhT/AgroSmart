package com.example.agrosmart.domain.models;

import java.util.List;

import io.realm.Realm;
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
    private String email;
    private String phoneNumber;
    private String municipality;
    private RealmList<String> soilTypes;
    private String status;
    private String role;

    public UserDetails(String username, String _email, String phoneNumber,
                       String municipality, RealmList<String> soilTypes,
                       String _status, String _role) {
        this.username = username;
        this.email = _email;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes;
        this.status = _status;
        this.role = _role;
    }

    public UserDetails(){}

}
