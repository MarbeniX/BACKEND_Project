package com.backend.projectbackend.dto.user;

public class CloudinaryImageDTO {
    private String url;
    private String publicID;

    public CloudinaryImageDTO() {}

    public CloudinaryImageDTO(String url, String publicID) {
        this.url = url;
        this.publicID = publicID;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }
}
