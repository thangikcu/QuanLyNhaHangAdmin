package com.coffeehouse.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class WorkingReport {
    private long id;
    private Date startDate;
    private Date endDate;
    @SerializedName("status")
    private boolean validate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
