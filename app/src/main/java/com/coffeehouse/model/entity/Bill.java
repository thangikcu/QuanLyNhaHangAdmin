package com.coffeehouse.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bill {
    private String createDate;
    private String id;
    private String deskId;
    @SerializedName("orderDetailDtos")
    private List<OrderDetail> orderDetails;
    private long totalPrice;
    @SerializedName("employeeDto")
    private User user;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String value) {
        this.createDate = value;
    }

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> value) {
        this.orderDetails = value;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long value) {
        this.totalPrice = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

}

