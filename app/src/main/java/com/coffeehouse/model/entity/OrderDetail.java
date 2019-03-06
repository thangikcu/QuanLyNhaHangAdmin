package com.coffeehouse.model.entity;

public class OrderDetail {
    private long drinkId;
    private String drinkName;
    private long id;
    private long price;
    private long count;

    public long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(long value) {
        this.drinkId = value;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String value) {
        this.drinkName = value;
    }

    public long getID() {
        return id;
    }

    public void setID(long value) {
        this.id = value;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long value) {
        this.price = value;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
