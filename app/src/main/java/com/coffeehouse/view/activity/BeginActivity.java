package com.coffeehouse.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coffeehouse.AppInstance;
import com.coffeehouse.view.dialog.NotifiDialog;


public class BeginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private NotifiDialog notifiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppInstance.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

}

