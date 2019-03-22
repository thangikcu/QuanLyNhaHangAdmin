package com.coffeehouse.model.entity;

import java.util.Date;

public class TurnOverDetail {
    private Date createDate;
    private String createID;
    private String createName;
    private String orderId;
    private long totalPrice;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date value) {
        this.createDate = value;
    }

    public String getCreateID() {
        return createID;
    }

    public void setCreateID(String value) {
        this.createID = value;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String value) {
        this.createName = value;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String value) {
        this.orderId = value;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long value) {
        this.totalPrice = value;
    }
}
