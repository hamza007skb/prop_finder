package com.example.propfinder.businessLogic;

import java.util.ArrayList;

public class Property {
    private final PROPERTY_TYPE propertyType;
    private final PROPERTY_STATUS propertyStatus;
    private final double PROPERTY_AREA;
    private String propertyAddress;
    private final Location location;
    private final double PROPERTY_PRICE;
    private ArrayList<String> propertyImages;
    private SELL_STATUS sellStatus;
    private String noOfRooms;
    private String SellerName;
    private String SellerContact;
    private String ownerEmail;
    public Property(String ownerEmail, double PROPERTY_PRICE, PROPERTY_TYPE propertyType, PROPERTY_STATUS propertyStatus, double PROPERTY_AREA, Location location, String rooms, ArrayList<String> propertyImages){
        this.ownerEmail = ownerEmail;
        this.propertyImages = propertyImages;
        this.PROPERTY_PRICE = PROPERTY_PRICE;
        this.propertyStatus = propertyStatus;
        this.propertyType = propertyType;
        this.PROPERTY_AREA = PROPERTY_AREA;
        this.location = location;
        this.sellStatus = SELL_STATUS.NOT_SOLD;
        this.noOfRooms = rooms;
    }
    public String getNoOfRooms() {
        return noOfRooms;
    }
    @Override
    public String toString() {
        return "Property{" +
                "propertyType=" + propertyType +
                ", propertyStatus=" + propertyStatus +
                ", PROPERTY_AREA=" + PROPERTY_AREA +
                ", propertyAddress='" + propertyAddress + '\'' +
                ", location=" + location + '\'' +
                ", PROPERTY_PRICE=" + PROPERTY_PRICE + '\'' +
                ", sellStatus=" + sellStatus +
                ", noOfBedrooms='" + noOfRooms + '\'' +
                ", SellerName='" + SellerName + '\'' +
                ", SellerContact='" + SellerContact + '\'' +
                '}';
    }
    public String getPropertyAddress() {
        return propertyAddress;
    }
    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }
    public void setSellerName(String sellerName) {
        this.SellerName = sellerName;
    }
    public void setSellerContact(String sellerContact) {
        this.SellerContact = sellerContact;
    }
    public String getSellerName() {
        return SellerName;
    }
    public String getSellerContact() {
        return SellerContact;
    }
    public void addPropertyImage(String image) {
        this.propertyImages.add(image);
    }

    public PROPERTY_TYPE getPropertyType() {
        return propertyType;
    }

    public PROPERTY_STATUS getPropertyStatus() {
        return propertyStatus;
    }

    public double getPROPERTY_AREA() {
        return PROPERTY_AREA;
    }

    public double getPROPERTY_PRICE() {
        return PROPERTY_PRICE;
    }

    public ArrayList<String> getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(ArrayList<String> propertyImages) {
        this.propertyImages = propertyImages;
    }

    public SELL_STATUS getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(SELL_STATUS sellStatus) {
        this.sellStatus = sellStatus;
    }

    public Location getLocation() {
        return location;
    }

    public static PROPERTY_STATUS propertyStatusSetter(String status){
        if (status != null){
            if (status.equals("For Sale"))
                return PROPERTY_STATUS.FOR_SALE;
            else
                return PROPERTY_STATUS.RENTAL;
        }
        return null;
    }
    public static PROPERTY_TYPE propertyTypeSetter(String type){
        if (type != null){
            if (type.equals("House"))
                return PROPERTY_TYPE.HOUSE;
            else if (type.equals("Apartment"))
                return PROPERTY_TYPE.APARTMENT;
            else
                return PROPERTY_TYPE.PLOT;
        }
        return null;
    }
}

