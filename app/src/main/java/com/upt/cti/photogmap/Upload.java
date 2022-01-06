package com.upt.cti.photogmap;

public class Upload {
    private String photoName;
    private String photoDescription;

    private String photoUrl;

    public Upload(){

    }

    public Upload(String photoName, String photoDescription, String photoUrl){
        if (photoName.trim().equals("")){
            photoName = "No Name";
        }

        this.photoName = photoName;
        this.photoDescription = photoDescription;
        this.photoUrl = photoUrl;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
