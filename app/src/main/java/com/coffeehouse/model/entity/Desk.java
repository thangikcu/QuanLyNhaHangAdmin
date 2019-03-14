package com.coffeehouse.model.entity;

import com.coffeehouse.R;
import com.coffeehouse.util.Utils;
import com.google.gson.annotations.SerializedName;

public class Desk {
    private String id;
    @SerializedName("status")
    private boolean serving;
    @SerializedName("isRemove")
    private boolean remove;

    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isServing() {
        return serving;
    }

    public void setServing(boolean serving) {
        this.serving = serving;
    }

//    public String getDeskName() {
//        return "Bàn " + id;
//    }

    public int getIdResBgBan() {
        if (serving) {
            return R.drawable.table_button_background_serving;
        } else {
            return R.drawable.table_button_background_free;
        }
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStringTrangThai() {
        if (serving) return Utils.getStringByRes(R.string.dang_phuc_vu);
        else return Utils.getStringByRes(R.string.trong);
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
}
