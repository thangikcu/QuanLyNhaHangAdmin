package com.coffeehouse.model.entity;

public class User {
    private String avatarData;
    private String dob;
    private String fullName;
    private String id;
    private String phoneNumber;
    private String rule;
    private long salary;
    private String status;

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

    public boolean isAdmin(){
        return rule.equals("ADMIN");
    }
}
