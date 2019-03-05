package com.coffeehouse.model.entity;

import com.google.gson.annotations.SerializedName;

import com.coffeehouse.R;

public class Desk {
    private String id;
    @SerializedName("status")
    private boolean serving;

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

    public String getTenBan() {
        return "Bàn " + id;
    }

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
}
