package com.example.agrosmart.domain.designModels;

public class ProfileDetails {

    private int imageResource;
    private String profileName;
    private String profileEmail;

    public ProfileDetails(int imageResource, String profileName, String profileEmail) {
        this.imageResource = imageResource;
        this.profileName = profileName;
        this.profileEmail = profileEmail;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }
}
