package com.coffeehouse.model.entity;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    public static final String MANAGER = "MANAGER";
    public static final String EMPLOYEE = "EMPLOYEE";

    private String avatarData;
    private String dob;
    private String fullName;
    private String password;
    private String id;
    private String phoneNumber;
    private String rule;
    private long salary;
    private int workingHours;
    private double salaryPerMonth;
    private String status;
    @SerializedName("dtos")
    private List<WorkingReport> listWorkingReport;

    public String getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(String value) {
        this.avatarData = value;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String value) {
        this.dob = value;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String value) {
        this.fullName = value;
    }

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String value) {
        this.rule = value;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long value) {
        this.salary = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public boolean isAdmin() {
        return rule.equals("MANAGER");
    }

    public List<WorkingReport> getListWorkingReport() {
        return listWorkingReport;
    }

    public void setListWorkingReport(List<WorkingReport> listWorkingReport) {
        this.listWorkingReport = listWorkingReport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImageToShow() {
        if (!TextUtils.isEmpty(avatarData)) {
            return Base64.decode(avatarData, Base64.DEFAULT);
        }
        return null;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public double getSalaryPerMonth() {
        return salaryPerMonth;
    }

    public void setSalaryPerMonth(double salaryPerMonth) {
        this.salaryPerMonth = salaryPerMonth;
    }
/*
    public Date getDobInDate() {
        try {
            return SimpleDateFormat.getTimeInstance().parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Calendar.getInstance().getTime();
    }*/
}
