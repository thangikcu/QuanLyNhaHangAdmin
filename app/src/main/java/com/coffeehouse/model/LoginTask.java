package com.coffeehouse.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.coffeehouse.model.entity.Admin;

/**
 * Created by Thanggun99 on 19/03/2017.
 */

public class LoginTask {
    public static final String AUTO = "AUTO";
    public static final String NOT_AUTO = "NOT_AUTO";

    public static final String FAIL = "FAIL";
    public static final String OTHER = "OTHER";
    public static final String SUCCESS = "SUCCESS";

    private OnLoginListener onLoginListener;

    private Admin admin;

    public void loginAuto() {
       /* if (isGhiNhoDangNhap()) {
            admin = new Admin();
            admin.setTenDangNhap(AppInstance.getPreferences().getString(USERNAME, null));
            admin.setMatKhau(AppInstance.getPreferences().getString(Admin.PASSWORD, null));
            admin.setKieuDangNhap(AUTO);

            new LoginAsyncTask().execute();
        }*/
    }


    public boolean isGhiNhoDangNhap() {
        return false;/*!TextUtils.isEmpty(AppInstance.getPreferences().getString(USERNAME, null));*/
    }

    public void login(Admin admin) {

        this.admin = admin;
        new LoginAsyncTask().execute();
    }

    public Admin getAdmin() {
        return admin;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            onLoginListener.onStartTask();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            switch (s) {
                case SUCCESS:
                    onLoginListener.onLoginSuccess();
                    break;
                case OTHER:
                    onLoginListener.onOtherLogin();
                    break;
                default:
                    onLoginListener.onLoginFail();
                    break;
            }
            onLoginListener.onFinishTask();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            delay(2000);
            return admin.login();
        }
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }


    public interface OnLoginListener {
        void onStartTask();

        void onFinishTask();

        void onLoginSuccess();

        void onLoginFail();

        void onOtherLogin();
    }
}
