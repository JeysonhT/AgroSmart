package com.example.agrosmart.data.network.dto;

import java.util.List;

public class UserDetailsDto {
    private String _id;
    private String username;
    private String phoneNumber;
    private String municipality;
    private List<String> soilTypes;

    public UserDetailsDto(String _id, String username, String phoneNumber, String municipality, List<String> soilTypes) {
        this._id = _id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes;
    }

    public UserDetailsDto(String username, String phoneNumber, String municipality, List<String> soilTypes) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.municipality = municipality;
        this.soilTypes = soilTypes;
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
}
