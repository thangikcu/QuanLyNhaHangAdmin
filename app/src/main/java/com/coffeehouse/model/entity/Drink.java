package com.coffeehouse.model.entity;

public class Drink {
    private long beverageID;
    private String description;
    private long id;
    private String name;
    private long price;

    public long getBeverageID() {
        return beverageID;
    }

    public void setBeverageID(long value) {
        this.beverageID = value;
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
}
