package com.coffeehouse.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class GoodReceipt {

    @SerializedName("goodReceiptDetailDtos")
    private List<GoodReceiptDetail> goodReceiptDetail;
    @SerializedName("totalPrice")
    private int totalPrice;
    @SerializedName("employeeName")
    private String employeeName;
    @SerializedName("employeeId")
    private String employeeId;
    @SerializedName("createDate")
    private Date createDate;
    @SerializedName("id")
    private String id;

    public List<GoodReceiptDetail> getGoodReceiptDetail() {
        return goodReceiptDetail;
    }

    public void setGoodReceiptDetail(List<GoodReceiptDetail> goodReceiptDetail) {
        this.goodReceiptDetail = goodReceiptDetail;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
