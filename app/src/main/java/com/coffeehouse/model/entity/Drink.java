package com.coffeehouse.model.entity;

import android.text.TextUtils;
import android.util.Base64;

public class Drink {
    private long beverageId;
    private String description;
    private long id;
    private String name;
    private long price;
    private String drinkImage;

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

    public String getDrinkImage() {
        return drinkImage;
    }

    public void setDrinkImage(String drinkImage) {
        this.drinkImage = drinkImage;
    }

    public byte[] getImageToShow() {
        if (!TextUtils.isEmpty(drinkImage)) {
            return Base64.decode(drinkImage, Base64.DEFAULT);
        }
        return null;
    }
}
