package com.example.propfinder.businessLogic;

public class Agent extends Seller{
    private final String licenseNumber;
    private final String agencyName;
    private double commissionRate;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public Agent(String fullName, String e_mail, String phoneNumber, String licenseNumber, String agencyName, double commissionRate) {
        super(fullName, e_mail, phoneNumber);
        this.agencyName = agencyName;
        setCommissionRate(commissionRate);
        this.licenseNumber = licenseNumber;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }
    public double getCommissionRate() {
        return commissionRate;
    }
    public double priceAfterCommission(Property property){
        return getCommissionRate() * property.getPROPERTY_PRICE();
    }
}
