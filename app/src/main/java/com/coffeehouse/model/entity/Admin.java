package com.coffeehouse.model.entity;

import java.io.Serializable;

/**
 * Created by Thanggun99 on 19/03/2017.
 */

public class Admin implements Serializable {

    public static final String ADMIN = "ADMIN";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    private static int maAdmin;
    private static int maToken;
    private int type;
    private boolean ghiNho;
    private String kieuDangNhap;
    private String tenDangNhap;
    private String matKhau;

    public Admin() {
    }

    public void ghiNhoDangNhap() {
/*        SharedPreferences.Editor editor = AppInstance.getPreferences().edit();
        editor.putString(USERNAME, tenDangNhap);
        editor.putString(PASSWORD, matKhau);
        editor.apply();*/
    }

    public void huyGhiNhoDangNhap() {
        /*AppInstance.getPreferences().edit().clear().apply();*/
    }

    public static int getMaAdmin() {
        return maAdmin;
    }

    public void setMaAdmin(int maAdmin) {
        Admin.maAdmin = maAdmin;
    }

    public static int getMaToken() {
        return maToken;
    }

    public void setMaToken(int maToken) {
        Admin.maToken = maToken;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isGhiNho() {
        return ghiNho;
    }

    public void setGhiNho(boolean ghiNho) {
        this.ghiNho = ghiNho;
    }

    public String getKieuDangNhap() {
        return kieuDangNhap;
    }

    public void setKieuDangNhap(String kieuDangNhap) {
        this.kieuDangNhap = kieuDangNhap;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

}
