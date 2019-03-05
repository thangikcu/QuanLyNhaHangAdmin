package com.coffeehouse.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinkType {
    private String description;

    @SerializedName("drinkDtos")
    private List<Drink> listDrinks;
    private long id;
    private String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public List<Drink> getListDrinks() {
        return listDrinks;
    }

    public void setListDrinks(List<Drink> value) {
        this.listDrinks = value;
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
}
