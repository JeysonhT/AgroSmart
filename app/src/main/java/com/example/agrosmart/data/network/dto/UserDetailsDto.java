package com.example.agrosmart.data.network.dto;

import java.util.List;

public class UserDetailsDto {
    private String _id;
    private String username;
    private String email;
    private String phoneNumber;
    private String municipality;
    private List<String> soilTypes;
    private String status;
    private String role;

    public UserDetailsDto(String username, String phoneNumber,
                          String _email, String municipality, List<String> soilTypes, String _role,
                          String _status) {
        this.username = username;
        this.email = _email;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes;
        this.role = _role;
        this.status = _status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public List<String> getSoilTypes() {
        return soilTypes;
    }

    public void setSoilTypes(List<String> soilTypes) {
        this.soilTypes = soilTypes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
