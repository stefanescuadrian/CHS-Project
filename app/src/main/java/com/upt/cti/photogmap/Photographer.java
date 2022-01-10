package com.upt.cti.photogmap;

public class Photographer {

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNoOfVotes() {
        return noOfVotes;
    }

    public void setNoOfVotes(int noOfVotes) {
        this.noOfVotes = noOfVotes;
    }

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String mail;
    private int score;
    private int noOfVotes;
    private String county;
    private String country;
    private String locality;
    private String userId;

    public String getCounty() {
        return county;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Photographer() {

    }

    public Photographer(String photographerName, String photographerSurname, String photographerPhoneNumber, String photographerEmail, int photographerPoints, int photographerVotes, String photographerCounty, String photographerLocality, String photographerCountry, String userId) {

        this.firstName = photographerName;
        this.lastName = photographerSurname;
        this.phoneNumber = photographerPhoneNumber;
        this.mail = photographerEmail;
        this.score = photographerPoints;
        this.noOfVotes = photographerVotes;
        this.country = photographerCountry;
        this.county = photographerCounty;
        this.locality = photographerLocality;
        this.userId = userId;
    }








}
