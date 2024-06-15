package com.example.propfinder;

public class PropertyModel {
    private String imageUrl;
    private String Price;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public PropertyModel(String imageUrl, String price) {
        this.imageUrl = imageUrl;
        Price = price;
    }
}
