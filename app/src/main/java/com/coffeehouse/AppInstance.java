package com.coffeehouse;

import android.app.Application;
import android.content.Context;

import com.coffeehouse.model.entity.User;
import com.coffeehouse.util.CoffeeStorage;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Thanggun99 on 19/11/2016.
 */

public class AppInstance extends Application {
    private static Context context;

    private static User loginUser;

    public static void saveLoginUser(User loginUser, boolean ghiNho) {
        AppInstance.loginUser = loginUser;

        if(ghiNho){
            CoffeeStorage.getInstance().saveLoginUser(loginUser);
        }
    }

    public static void clearLoginSection() {
        loginUser = null;
        CoffeeStorage.getInstance().saveLoginUser(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this;

        loginUser = CoffeeStorage.getInstance().getLoginUser();
    }

    public static Context getContext() {
        return context;
    }


    public static User getLoginUser() {
        return loginUser;
    }

    public static boolean isLogin(){
        return loginUser != null;
    }
}
