package com.coffeehouse.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.coffeehouse.AppInstance;
import com.coffeehouse.model.entity.User;

public class CoffeeStorage {
    private static CoffeeStorage instance;


    private final SharedPreferences preferences;

    private CoffeeStorage() {
        preferences = AppInstance.getContext().getSharedPreferences("TheCoffee", Context.MODE_PRIVATE);
    }

    public static CoffeeStorage getInstance() {
        if(instance == null){
            instance = new CoffeeStorage();
        }

        return  instance;
    }

    private final String LOGIN_USER = "LOGIN_USER";


    public void saveLoginUser(User loginUser) {
        preferences.edit().putString(LOGIN_USER, new Gson().toJson(loginUser)).apply();
    }
    public User getLoginUser() {
        return new Gson().fromJson(preferences.getString(LOGIN_USER, null), User.class);
    }
}
