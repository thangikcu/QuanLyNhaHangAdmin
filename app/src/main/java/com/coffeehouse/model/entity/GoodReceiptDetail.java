package com.coffeehouse.model.entity;

import com.google.gson.annotations.SerializedName;

public class GoodReceiptDetail {
    @SerializedName("price")
    private int price;
    @SerializedName("amount")
    private String amount;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
