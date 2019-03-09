package com.coffeehouse.model.entity;

import android.util.Base64;

public class Drink {
    private long beverageId;
    private String description;
    private long id;
    private String name;
    private long price;
    private String image;

    public long getBeverageId() {
        return beverageId;
    }

    public void setBeverageId(long value) {
        this.beverageId = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public long getID() {
        return id;
    }

    public void setID(long value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long value) {
        this.price = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public byte[] getImageToShow() {
        return Base64.decode(image, Base64.DEFAULT);
    }
}
