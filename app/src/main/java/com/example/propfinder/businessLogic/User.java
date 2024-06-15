package com.example.propfinder.businessLogic;

public abstract class User {
    private String fullName;
    private final String e_mail;
    private String phoneNumber;

    protected User(String fullName, String e_mail, String phoneNumber) {
        this.fullName = fullName;
        this.e_mail = e_mail;
        this.phoneNumber = phoneNumber;
    }

    public String getE_mail() {
        return e_mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName(){
        return fullName;
    }
}
