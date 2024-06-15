package com.example.propfinder.businessLogic;

import java.util.ArrayList;

abstract class Seller extends User{
    private ArrayList<Property> properties;
    public Seller(String fullName, String e_mail, String phoneNumber) {
        super(fullName, e_mail, phoneNumber);
        properties = new ArrayList<>();
    }
    public ArrayList<Property> getProperties() {
        return properties;
    }
    public void addProperty(Property property) {
        this.properties.add(property);
    }
}
