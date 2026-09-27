package com.example.agrosmart.domain.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class UserDetails {
    private String _id;
    private String username;
    private String email;
    private String phoneNumber;
    private String municipality;
    private List<String> soilTypes = new ArrayList<>();
    private String status;
    private String role;

    public UserDetails(String username, String _email, String phoneNumber,
                       String municipality, List<String> soilTypes,
                       String _status, String _role) {
        this.username = username;
        this.email = _email;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes != null ? soilTypes : new ArrayList<>();
        this.status = _status;
        this.role = _role;
    }

    public UserDetails(){}

    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getMunicipality() { return municipality; }
    public void setMunicipality(String municipality) { this.municipality = municipality; }

    public List<String> getSoilTypes() { return soilTypes; }
    public void setSoilTypes(List<String> soilTypes) { this.soilTypes = soilTypes != null ? soilTypes : new ArrayList<>(); }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
