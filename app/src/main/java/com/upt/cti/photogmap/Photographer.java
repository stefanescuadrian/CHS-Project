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

    public Photographer() {

    }

    public Photographer(String photographerName, String photographerSurname, String photographerPhoneNumber, String photographerEmail, int photographerPoints, int photographerVotes) {

        this.firstName = photographerName;
        this.lastName = photographerSurname;
        this.phoneNumber = photographerPhoneNumber;
        this.mail = photographerEmail;
        this.score = photographerPoints;
        this.noOfVotes = photographerVotes;
    }








}
